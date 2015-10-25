# nats-messaging-for-flink ![build status](https://travis-ci.org/mring33621/nats-messaging-for-flink.svg?branch=master)
[NATS Messaging](http://nats.io/) SourceFunctions and SinkFunctions for [Apache Flink](http://flink.apache.org/) Streaming.

Version 0.4.1

ABOUT:
* Allows Apache Flink to receive a stream of string based messages from a NATS messaging topic.
* Allows Apache Flink to send a stream of string based messages to a NATS messaging topic.
* Tested on Flink v0.9.1 streaming and gnatsd Server v0.6.8
* Uses an embedded copy of https://github.com/mring33621/java_nats, which is a fork of https://github.com/tyagihas/java_nats, with some bug fixes.
* Java 7 or 8 compatible. Not sure about Java 6.

USAGE:
* Build it (with Maven)
* Give an instance of NatsSource to your Flink StreamExecutionEnvironment, then Map and Filter the resulting DataStreamSource<String> to your heart's content!

FUTURE:
* ~~~Add a NatsSink class, to send messages to a NATS topic from Flink jobs~~~
* Add support for non-String message types

LICENSE:
Apache 2.0 licensed.
