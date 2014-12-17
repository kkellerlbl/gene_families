#!/bin/bash
cd ./data
mkdir tmp
cd ./tmp
unamestr=`uname`

if [[ "$unamestr" == 'Linux' ]]; then
    OS='linux'
elif [[ "$unamestr" == 'Darwin' ]]; then
    OS='macos'
fi

########### Pfam #############
if [ ! -f ../db/Pfam-A.hmm ]; then
    echo "Downloading Pfam..."
    curl -o ../db/Pfam-A.full.gz 'ftp://ftp.ebi.ac.uk/pub/databases/Pfam/releases/Pfam27.0/Pfam-A.full.gz'
    curl -o ../db/Pfam-A.hmm.gz 'ftp://ftp.ebi.ac.uk/pub/databases/Pfam/releases/Pfam27.0/Pfam-A.hmm.gz'
    gzip -d ../db/Pfam-A.hmm.gz
    ../bin/hmmpress.$OS ../db/Pfam-A.hmm
fi

########### TIGRFAMS #############
if [ ! -f ../db/TIGRFAMs_15.0_HMM.LIB ]; then
    echo "Downloading Pfam..."
    curl -o ../db/TIGRFAMs_15.0_HMM.LIB.gz 'ftp://ftp.jcvi.org/pub/data/TIGRFAMs/TIGRFAMs_15.0_HMM.LIB.gz'
    gzip -d ../db/TIGRFAMs_15.0_HMM.LIB.gz
    ../bin/hmmpress.$OS ../db/TIGRFAMs_15.0_HMM.LIB
fi

########### CDD #############
if [ ! -f ../db/cdd.hmm ]; then
    echo "Downloading CDD..."
    curl -o ../db/cddid.tbl.gz 'ftp://ftp.ncbi.nlm.nih.gov/pub/mmdb/cdd/cddid.tbl.gz'
    curl -o cdd.tar.gz 'ftp://ftp.ncbi.nlm.nih.gov/pub/mmdb/cdd/cdd.tar.gz'
    mkdir smp
    cd smp
    tar xf ../cdd.tar.gz
    ls -1 cd*.smp > Cdd
    ls -1 smart*.smp > Smart
    ls -1 cog*.smp > Cog
    ../../bin/makeprofiledb.$OS -i Cdd -threshold 9.82 -scale 100.0 -dbtype rps -index true
    ../../bin/makeprofiledb.$OS -i Cog -threshold 9.82 -scale 100.0 -dbtype rps -index true
    ../../bin/makeprofiledb.$OS -i Smart -threshold 9.82 -scale 100.0 -dbtype rps -index true
    mv Cdd.* ../../db
    mv Smart.* ../../db
    mv Cog.* ../../db
    cd ..
    rm -rf smp
fi
