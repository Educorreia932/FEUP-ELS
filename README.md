# Software Language Engineering

## Group 04

- Ângela Coelho (up201907549@edu.fe.up.pt)
- Eduardo Correia (up201806433@edu.fe.up.pt)
- Tomás Torres (up201800700@edu.fe.up.pt)

## Table Fork

![](logo.png)

Table Fork is a DSL for tabular data extraction and manipulation from arbitrary sources.

## Checkpoints

[Checkpoint 1](./docs/checkpoints/Checkpoint_1.md): 2023/10/03

[Checkpoint 2](./docs/checkpoints/Checkpoint_2.md): 2023/11/07

[Checkpoint 3](./docs/checkpoints/Checkpoint_3.md): 2023/12/12

## Project setup

For this project, you need to [install Gradle](https://gradle.org/install/)

Copy your source files to the ``src`` folder, and your JUnit test files to the ``test`` folder.

## Compile and Running

To compile and install the program, run ``gradle installDist``. This will compile your classes and create a launcher
script in the folder ``build/install/els2023-4/bin``. For convenience, there are two script files, one for
Windows (``els2023-4.bat``) and another for Linux (``els2023-4``), in the root of the repository, that call these
scripts.

After compilation, tests will be automatically executed, if any test fails, the build stops. If you want to ignore the
tests and build the program even if some tests fail, execute Gradle with flags "-x test".

When creating a Java executable, it is necessary to specify which class that contains a ``main()`` method should be
entry point of the application. This can be configured in the Gradle script with the property ``mainClassName``, which
by default has the value ``pt.up.fe.els2023.Main``.

## Test

To test the program, run ``gradle test``. This will execute the build, and run the JUnit tests in the ``test`` folder.
If you want to see output printed during the tests, use the flag ``-i`` (i.e., ``gradle test -i``).
You can also see a test report by opening ``build/reports/tests/test/index.html``.
