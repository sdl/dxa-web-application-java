#!/bin/bash

###
### A bash script to simplify Maven releases.
###

# Make sure no pending change is on the working directory
modified_count=`git status --porcelain | wc -l`
if [[ "${modified_count}" -ne "0" ]]; then
   echo "Your working directoy contains uncommitted changes."
   exit 1
fi

# Make sure no pull can be made before release
LOCAL_ORIGIN=$(git rev-parse @)
REMOTE_ORIGIN=$(git rev-parse @{u})
if [[ "${LOCAL_ORIGIN}" != "${REMOTE_ORIGIN}" ]]; then
   echo "Your local branch is not synchronized with its remote branch."
   exit 1
fi

# How to use the script
function usage {
   echo "Release using Apache Maven"
   echo ""
   echo "Usage:"
   echo "   release.sh REL_VERSION DEV_VERSION GPG_KEYNAME GPG_PASS"
   echo ""
   echo "      REL_VERSION    The version to use for the release"
   echo "      DEV_VERSION    The version to use for the development"
   echo "      GPG_KEYNAME    The GPG key name"
   echo "      GPG_PASS       The GPG passphrase"
   echo ""
}

if [[ $# -lt "4" ]]; then
   usage
   exit 1
fi

REL_VERSION="$1"
DEV_VERSION="$2"
GPG_KEYNAME="$3"
GPG_PASS="${4}"
REL_TAG="DXA_${REL_VERSION}"

mvn clean
mvn -Prelease release:prepare -DreleaseVersion="${REL_VERSION}" -DdevelopmentVersion="${DEV_VERSION}" -Dtag=${REL_TAG} -Darguments="-Dgpg.passphrase=${GPG_PASS} -Dgpg.keyname=${GPG_KEYNAME} -Dgpg.executable=./gpgwrapper.sh"
mvn -Prelease release:perform -DreleaseVersion="${REL_VERSION}" -DdevelopmentVersion="${DEV_VERSION}" -Dtag=${REL_TAG} -Darguments="-Dgpg.passphrase=${GPG_PASS} -Dgpg.keyname=${GPG_KEYNAME} -Dgpg.executable=./gpgwrapper.sh"
mvn -Prelease deploy -Dgpg.passphrase=${GPG_PASS} -Dgpg.keyname=${GPG_KEYNAME} -Dgpg.executable=gpg -Dgpg.executable=./gpgwrapper.sh

echo "Released version:       ${REL_VERSION}"
echo "Release tag:            ${REL_TAG}"
echo "Development version:    ${DEV_VERSION}"
