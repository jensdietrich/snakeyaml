## DOS Prevention

To prevent billion-laugh based DOS attacks , some control parameters that restrict the size of data structures created are used. 
Those are defined in `org.yaml.snakeyaml.LoaderOptions`. Those are likely the defenses intricuded after we reported
the vulnerability.  To enable `payloads/hash-key/lol50.yaml`, `nestingDepthLimit` and `maxAliasesForCollections` values have to be increased -- to 100. 

The run this as follows: 
1. build project with `mvn compile`
2. run payload `java -cp target/classes Driver dos-payloads/hash-key/lol50.yaml` (tested with Java 11)


### Constraint Enforcement Stacktrace 
```
Exception in thread "main" org.yaml.snakeyaml.error.YAMLException: Nesting Depth exceeded max 50
	at org.yaml.snakeyaml.composer.Composer.increaseNestingDepth(Composer.java:377)
	at org.yaml.snakeyaml.composer.Composer.composeNode(Composer.java:202)
        ...
	at org.yaml.snakeyaml.composer.Composer.composeSequenceNode(Composer.java:277)
	at org.yaml.snakeyaml.composer.Composer.composeNode(Composer.java:207)
	at org.yaml.snakeyaml.composer.Composer.composeKeyNode(Composer.java:359)
	at org.yaml.snakeyaml.composer.Composer.composeMappingChildren(Composer.java:344)
	at org.yaml.snakeyaml.composer.Composer.composeMappingNode(Composer.java:323)
	at org.yaml.snakeyaml.composer.Composer.composeNode(Composer.java:209)
	at org.yaml.snakeyaml.composer.Composer.getNode(Composer.java:131)
	at org.yaml.snakeyaml.composer.Composer.getSingleNode(Composer.java:157)
	at org.yaml.snakeyaml.constructor.BaseConstructor.getSingleData(BaseConstructor.java:178)
	at org.yaml.snakeyaml.Yaml.loadFromReader(Yaml.java:493)
	at org.yaml.snakeyaml.Yaml.load(Yaml.java:446)
	at Driver.main(Driver.java:15)
```

### Trigger and Traversal

The first part of this stack trace describes the path to the traversal method. 

```
    at org.yaml.snakeyaml.composer.Composer.composeSequenceNode(Composer.java:277)
	at org.yaml.snakeyaml.composer.Composer.composeNode(Composer.java:209)
	at org.yaml.snakeyaml.composer.Composer.getNode(Composer.java:131)
	at org.yaml.snakeyaml.composer.Composer.getSingleNode(Composer.java:157)
	at org.yaml.snakeyaml.constructor.BaseConstructor.getSingleData(BaseConstructor.java:178)
	at org.yaml.snakeyaml.Yaml.loadFromReader(Yaml.java:493)
	at org.yaml.snakeyaml.Yaml.load(Yaml.java:446)
	at Driver.main(Driver.java:15)
```

### Static Analysis Shortcomings

The [existing analyses](https://bitbucket.org/unshorn/ttt-extended/src/main/analysis_output/snakeyaml/) correctly identifies
the composite, but not the traversal. 

The reference to the composite in the traversal are not in arguments (including `this` considered as arg-0),
but in the return as the composite structure is constructed in `org.yaml.snakeyaml.composer.Composer`
using the recursive interaction between `composeNode` and `composeSequenceNode`. 

It should be possible to modify the analysis accordingly. 