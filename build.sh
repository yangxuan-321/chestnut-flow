#!/bin/bash

set -e

if [ ! $1 ]; then
    echo "error!"
    exit 1
fi

export HADOOP_USER_NAME=root

d=$(date -u +"%Y-%m-%dT%H%M%SZ")

SBT_OPTS="-Xms4096M -Xmx4096M -Xss16M" sbt -Dsbt.override.build.repos=true -Dconf=$1 clean core/universal:packageZipTarball cache/universal:packageZipTarball

p1=${CI_PROJECT_NAMESPACE}/${CI_PROJECT_NAME}/${CI_COMMIT_REF_NAME}/${CI_COMMIT_SHA}/$1

localdir=/home/output/$p1

cd ./core/target/universal/

for i in $(ls . | grep -v SHA1SUMS)
do
    if [ -f $i ] ; then
        echo $(sha1sum $i) >> SHA1SUMS
    fi
done

cd -

cd ./cache/target/universal/

for i in $(ls . | grep -v SHA1SUMS)
do
    if [ -f $i ] ; then
        echo $(sha1sum $i) >> SHA1SUMS
    fi
done

cd -

mkdir -pv $localdir/{core,cache}

cp -vr core/target/universal/* $localdir/core/
cp -vr cache/target/universal/* $localdir/cache/

touch core/target/universal/${d}.txt
touch cache/target/universal/${d}.txt

rm -vfr core/target/universal/tmp
rm -vfr cache/target/universal/tmp

echo "build completed."
echo "packages in local: $localdir"

exit 0
