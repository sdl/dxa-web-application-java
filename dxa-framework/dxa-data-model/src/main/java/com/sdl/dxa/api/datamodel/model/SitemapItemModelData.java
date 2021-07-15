package com.sdl.dxa.api.datamodel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ComparisonChain;
import com.sdl.dxa.api.datamodel.json.Polymorphic;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@Data
@ToString(exclude = "parent")
@EqualsAndHashCode(exclude = "parent")
@Accessors(chain = true)
@JsonTypeName
@Polymorphic
public class SitemapItemModelData implements Comparable<SitemapItemModelData>, JsonPojo {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Title")
    @Setter(AccessLevel.NONE)
    private String title;

    @JsonProperty("OriginalTitle")
    private String originalTitle;

    @JsonProperty(value = "Url", defaultValue = "")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String url;

    @JsonProperty("Visible")
    private boolean visible;

    @JsonProperty("Items")
    @Setter(AccessLevel.NONE)
    private SortedSet<SitemapItemModelData> items = new TreeSet<>();

    @JsonProperty("PublishedDate")
    private DateTime publishedDate;

    @JsonIgnore
    private boolean parentsSet = false;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    private SitemapItemModelData parent;

    /**
     * Adds an item to a collection of items and initializes it if needed.
     *
     * @param item item to add
     * @return itself
     */
    @NotNull
    public SitemapItemModelData addItem(SitemapItemModelData item) {
        if (this.items == null) {
            this.items = new TreeSet<>();
        }
        item.parent = this;
        this.items.add(item);
        return this;
    }

    public SitemapItemModelData setItems(Collection<SitemapItemModelData> items) {
        if (items == null) {
            this.items = null;
        } else {
            items.forEach(this::addItem);
        }
        return this;
    }

    @Override
    public int compareTo(@NotNull SitemapItemModelData o) {
        ComparisonChain chain = ComparisonChain.start();
        if (this.getOriginalTitle() == null || o.getOriginalTitle() == null) {
            chain = chain.compareFalseFirst(this.getOriginalTitle() == null, o.getOriginalTitle() == null);
        } else {
            chain = chain.compare(this.getOriginalTitle(), o.getOriginalTitle());
        }
        return chain.compare(this.getId(), o.getId()).result();
    }

    /**
     * Setter for title which also sets original title <strong>if original title is not yet set</strong>.
     *
     * @param title title to set
     */
    public SitemapItemModelData setTitle(String title) {
        this.title = title;
        if (this.originalTitle == null) {
            setOriginalTitle(title);
        }
        return this;
    }

    /**
     * Finds a SitemapItemModelData with a given URL path in the Sitemap subtree rooted by this {@link SitemapItemModelData}.
     *
     * @param urlToFind The URL path to search for
     * @return a {@link SitemapItemModelData} with the given URL path or <code>null</code> if no such item is found
     */
    @Nullable
    public SitemapItemModelData findWithUrl(String urlToFind) {
        if (getUrl() != null && urlToFind.matches(getUrl() + "/?")) {
            return this;
        }

        for (SitemapItemModelData item : getItems()) {
            item.setParent(this);
            SitemapItemModelData withUrl = item.findWithUrl(urlToFind);
            if (withUrl != null) {
                return withUrl;
            }
        }

        return null;
    }

    public void rebuildParentRelationships() {
        for (SitemapItemModelData child : getItems()) {
            child.setParent(this);
            child.rebuildParentRelationships();
        }
        parentsSet = true;
    }

    public SitemapItemModelData getParent() {
        if (!parentsSet) {
            throw new RuntimeException("The parents have not been computed; please call rebuildParentRelationships() on the root parent first.");
        }
        return parent;
    }
}
