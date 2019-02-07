## Test Results: macOS Mojave Version 10.14.2

### Stop method: HALT

```
Result: TestContext(number=2224, success=891, failure=1333, lastId=28513)
```

Interesting observation that auto generated IDs of inserted entity when test successful was increased with step higher than 1.
See logs:
* [parent-2019-01-18.log.gz](results/macOS-10-14-2/halt/parent-2019-01-18.log.gz)
* [parent-2019-01-19.log.gz](results/macOS-10-14-2/halt/parent-2019-01-19.log.gz)

### Stop method: EXIT

```
Result: TestContext(number=2474, success=2474, failure=0, lastId=30987)
```

In this test all IDs of inserted entities are contiguous (with step 1).

The reason why `lastId` doesn’t match `number` of tests is that test was started with not fresh database, but with database left in state after previous test with halt stop method.

See logs:
* [parent-2019-01-19.log.gz](results/macOS-10-14-2/exit/parent-2019-01-19.log.gz)
* [parent-2019-01-20.log.gz](results/macOS-10-14-2/exit/parent-2019-01-20.log.gz)

## Test Results: Amazon Linux AMI Version 2018.03

On Amazon linux results are more or less the same with the only difference that failure rate is significantly less, but still present.

Fortunately with EXIT stop method there are no single lost data!

### Stop method: HALT

```
Result: TestContext(number=5227, success=5202, failure=25, lastId=166689)
```

See logs:
* [parent-2019-01-27.log.gz](results/ami-2018-03/halt/parent-2019-01-27.log.gz)

### Stop method: EXIT

```
Result: TestContext(number=6450, success=6450, failure=0, lastId=6451)
```

See logs:
* [parent-2019-02-07.log.gz](results/ami-2018-03/exit/parent-2019-02-07.log.gz)
