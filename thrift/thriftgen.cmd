#!/bin/sh

xrpcgen-0.9.0 --gen java *.thrift

cp gen-java/* ../src/ -rf
rm gen-java -rf

