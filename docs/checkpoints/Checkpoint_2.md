## Checkpoint 2

The objective of this checkpoint was read [vitis-report.xml](/test/resources/checkpoint2/data/vitis-report.xml), [decision_tree.yaml](/test/resources/checkpoint2/data/decision_tree.yaml) and [profiling.json](/test/resources/checkpoint2/data/profiling.json) and did the following:

- From `vitis-report.xml`: Extract the table under the element `<Resources>`, that is under the
element `<AreaEstimates>` (there is another element `<Resources>`, under the element `<Device>`)
- From `decision_tree.yaml`: Extract the key-value pairs at the root level whose values are not
composite types (example of composite type: object, array), as well as the key-values of the
object `params`
- From `profiling.json`: Extract the name of the function that took a higher percentage of
execution time, as well as the corresponding percentage
- Concatenate all these results in a single table with a single row(excluding header), with a new column at the beginning with the name of the folder that contains the
report files
- Save this table in a `HTML` file

### Semantic Model

![semantic model](/docs/images/semantic_model_cp2.png)

- [TerminalValues](/src/pt/up/fe/els2023/model/table/values/): Now columns have their value types, which can be `Double`, `Integer` or `String`.
- [CompositeValues](/src/pt/up/fe/els2023/model/table/values/TableValue.java): In tables where a column have a `TableValue`, it means that columns elements points to other tables.

### Program Flow

![Program Flow](/docs/images/program_flow_cp2.png)


### Internal DSL

[Internal DSL](/test/pt/up/fe/els2023/InternalTest.java) can be checked in these tests for all checkpoints.

#### Operations

New operations are created in addition to the previous checkpoint:

- **Import:** 
    - To support differents types of files(`YAML`, `JSON` and `XML`), the `Adapter Pattern` was used to construct this operation
    - After loading a file, a map with its content is yield

- **Join:**
    - For a need of different types of join operation, two new operations were created:
        - **Merge:** Joins two or more tables in a single one by their column
        - **Concatenate:** Joins two or more tables in a single one by their row

- **Field Selection:**
    - Selects an table by their field. E.g. if a table has columns A, B and C, if we want only columns A and B, this operation creates another table with these columns selected
    - If selects a column with `composite values` it yields their sub-columns

- **Type Selection:**
    - Selects only columns with a specific type and return a table with only these columns.

- **Renaming:** 
    - Renames a table name on header

- **Min/Max:**
    - Selects minimum or maximum values in a column and return a table with that row

- **Unflaten:**
    - Changes columns into rows and rows into columns
    
- **Export:**
    - Use `Adapter Pattern` to support differents types of files
    - Exports the table to an `HTML` file
