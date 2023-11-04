## Checkpoint 1

The objective of this checkpoint was read [decision_tree_1.yaml](/src/resources/checkpoint1/data/decision_tree_1.yaml), [decision_tree_2.yaml](/src/resources/checkpoint1/data/decision_tree_2.yaml) and [decision_tree_3.yaml](/src/resources/checkpoint1/data/decision_tree_3.yaml) and did the following:

- Select all the fields from `params`
- Create a column for each field
- Rename each column with this respective field name in a well format
- Add a new first column named "File" which contains file's name
- Create a row for each file with these param's fields content
- Save this table in a `.csv` format 

### Semantic Model

![semantic model](/docs/images/semantic_model_cp1.png)

- [DataSingleton](/src/pt/up/fe/els2023/model/DataContext.java): A class that contains all the tables and `FileData` objects in a list for each one. 
- [Table](/src/pt/up/fe/els2023/model/table/Table.java): A class that was made to do the language operations internally. It contains a list of [Columns](/src/pt/up/fe/els2023/model/table/Column.java) and a name.
- **FileData**: Data from the files that were loaded, each one, in a `Map` that stores the object and its data name.

### Program Flow

![Program Flow](/docs/images/program_flow_cp1.png)

[Config File](/src/resources/checkpoint1/config.yaml) -> [ConfigParser](/src/pt/up/fe/els2023/config/ConfigParser.java) -> [Load](/src/pt/up/fe/els2023/instructions/LoadInstruction.java) -> [Select](/src/pt/up/fe/els2023/instructions/SelectInstruction.java) -> [Merge](/src/pt/up/fe/els2023/instructions/MergeInstruction.java) -> [Save](/src/pt/up/fe/els2023/instructions/SaveInstruction.java) -> [Target](/src/resources/checkpoint1/result.csv)

[Sources](/src/resources/checkpoint1/data/) -> [Yaml Loader](/src/pt/up/fe/els2023/load/YamlLoader.java) -> [Data](/src/pt/up/fe/els2023/model/DataContext.java)

### Configuration File

```
source:
  - file: <filename>
commands:
  - !select
    select:
      - !metadataSelection
        metadata: filename
        rename: <column_name>
        
      - !fromSelection
        from: <field>
        keys:
          - name: <key>
            rename: <column_name>

  - !merge
    merge:
      - sources:
          - <table>
        target: <table>

target:
  - file: <filename>
    table: <column_name>
```
- **Fields**:
    - `source`: Select the file that will be loaded.
    - `commands`: A list of instructions that will be executed in order.
    - `target`: Create a new file with a `filename` in `.csv` format and select the `result table`.
- Custom tags to disambiguate polymorphism
- Data stored in a singleton file
- [Parsing](/src/pt/up/fe/els2023/load/YamlLoader.java) done using `SnakeYAML`

#### Instructions

Instructions are exectued one after other using `Command Pattern` design. These instructions were included in `commands` field in `configuration file`:

- **Load:** 
    - The type of the file to be loaded is obtained through its extension
    - For each file, a *Table* is created with the file name

- **Select:**
    - Columns are renamed as a parsing operation
    - The selection is done for every table/file by default (assuming they follow the same structure)

- **Metadata Selection:**
    - The metadata type to be accessed is determined by the `metadata` field
    - For now, only the filename is accessible metadata

- **From Selection:**
    - The `from` field specifies the path of the parent element form where the sub-element should be selected
    - The `keys` field specifies which sub-element should be selected

- **Merge:** 
    - Tables that have been selected by source parameter have a merge operation here into a new output table
    - The `sources` field selects which tables will be merge
    - The `target` field defines the name of the merged table

- **Save:**
    - Selected tables are exported to specified file using ***OpenCSV***
