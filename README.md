[![Build Status](https://travis-ci.org/msigwart/fakeload.svg?branch=master)](https://travis-ci.org/msigwart/fakeload)

# FakeLoad
FakeLoad is an open-source Java library which provides a simple and flexible way of producing “fake" system loads in applications or tests.

Producing system load using FakeLoad is as simple as this:
```java
// Create FakeLoad
FakeLoad fakeload = FakeLoads.create()
    .lasting(10, TimeUnit.SECONDS)
    .withCpu(80)
    .withMemory(300, MemoryUnit.MB);
 
// Execute FakeLoad synchronously
FakeLoadExecutor executor = FakeLoadExecutors.newDefaultExecutor();
executor.execute(fakeload);

// Execute FakeLoad asynchronously
Future<Void> execution = executor.executeAsync(fakeload);
execution.get(); // wait for completion
execution.cancel(); // cancel execution

// Shutdown executor
executor.shutdown();
```
The above snippet would simulate a CPU load of 80% and a memory load of 300 MB for ten seconds.

Check out the **[User Guide](https://github.com/msigwart/fakeload/wiki/User-Guide)** for more usage examples.

# Getting Started
You can add a dependency on FakeLoad using Maven with:
```xml
<dependency>
    <groupId>com.martensigwart</groupId>
    <artifactId>fakeload</artifactId>
    <version>0.6.0</version>
</dependency>
```
To add a dependency using Gradle, use this:
```
repositories {
    mavenCentral()
}

dependencies {
    compile 'com.martensigwart:fakeload:0.6.0'
}
```

> Note: In versions <= 0.4.0, the CPU load simulation does not work correctly if FakeLoad is executed in a containerized environment (e.g., Docker). This issue was [resolved](https://bugs.openjdk.java.net/browse/JDK-8228428) in Java 14. Thus, if you plan to use FakeLoad in containerized environment, you should use versions >= 0.5.0 and Java 14.

## Use Cases
FakeLoad was created with three different use cases in mind:

### 1. Testing of Non-Functional Requirements
Early verification of non-functional requirements like scalability, performance, availablity, etc. can be vital to a project's success. For example, an application or framework that does elastic data stream processing needs to make sure its auto-scaling behavior or monitoring is working correctly. This can be hard to test if the system is not fully developed yet as some "not yet" implemented parts of the system might have a huge impact on the those dynamic properties. 

FakeLoad can be used to simulate system behavior in missing parts. This way, instead of force-implementing missing parts or making risky assumptions about runtime behavior, developers can actually test dynamic properties of their systems.

### 2. Testing with Test Doubles (a.k.a. Mocks, Fakes, Dummies, etc.)
Replacing real objects with test doubles is a well-established practice in software testing, as they usually help to test components of a software system in isolation. However, replacing real objects with simpler 'fakes' can be dangerous when the simpler 'fake' object doesn't sufficiently mimick the more complex 'real' object or certain parts of it. 

FakeLoad can be used to mimick runtime characteristics like CPU usage, etc. within test doubles. As FakeLoad is designed to be easy-to-use, test doubles would remain simple while at the same time mimicking 'real' behavior.

### 3. Faking of "real" Data or Algorithms
Sometimes [NDA](https://en.wikipedia.org/wiki/Non-disclosure_agreement)s might prohibit publication or scientific evaluation involving certain algorithms or data. FakeLoad can be used to "simulate" data or an algorithm's behavior, bypassing the NDA and thus allowing publication.

# Resources
* [User Guide](https://github.com/msigwart/fakeload/wiki/User-Guide) - Information on general API usage and extensibility
* [Javadocs](https://www.javadoc.io/doc/com.martensigwart/fakeload/) - API Docs
* [Behind the Covers](https://www.researchgate.net/publication/328940284_FakeLoad_An_Open-Source_Load_Generator) - Technical report describing motivation, architecture, and evaluation of FakeLoad
* [FAQ](https://github.com/msigwart/fakeload/wiki/FAQ) - Frequently Asked Questions

# How to contribute
FakeLoad was created as part of a university project so any contribution is welcome. Feel free to report bugs, request features or submit a pull request.

# Acknowledgements
* This project was started as a support for [VISP](https://visp-streaming.github.io/) – An Ecosystem for Elastic Data Stream Processing for the Internet of Things. You can find the Github project [here](https://github.com/visp-streaming).
* CPU load generation was inspired by <https://caffinc.github.io/2016/03/cpu-load-generator/>.

# Licence
This project is licensed under the MIT License - see the [LICENCE.md](LICENSE.md) file for details


