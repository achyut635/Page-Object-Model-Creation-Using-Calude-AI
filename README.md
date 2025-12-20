# Page Object Model Creation — Using Claude AI

With **Allure reports** and **TestNG suites** (smoke, regression, parallel).

## Quick start
```bash
mvn -q -DskipTests dependency:resolve
# Run smoke suite
mvn -DtestngXml=src/test/resources/testng-smoke.xml test
# Run regression (parallel by tests)
mvn -DtestngXml=src/test/resources/testng-regression.xml test
# Run parallel by methods
mvn -DtestngXml=src/test/resources/testng-parallel.xml test
```

> You can also run: `mvn test` (uses default includes).

## Allure
Results are saved under `target/allure-results/`.
- Generate and open report locally:
```bash
mvn allure:serve
```
- Or generate static HTML in `target/site/allure-maven-plugin`:
```bash
mvn allure:report
```

## Headed/Headless
Suites accept a `headed` parameter (`true|false`). Example:
```bash
mvn -DtestngXml=src/test/resources/testng-smoke.xml -Dheaded=true test
```

## Suite files
- `src/test/resources/testng-smoke.xml`
- `src/test/resources/testng-regression.xml`
- `src/test/resources/testng-parallel.xml`

## Annotations
Tests include Allure metadata: `@Epic`, `@Feature`, `@Story`, `@Owner`, `@Description`. Failures auto-attach a screenshot and current URL.


---

## Groups (includes/excludes)
- **smoke**: quick checks
- **regression**: deeper flows
- You can also create custom groups, e.g. `product`, and include/exclude them in suites.

### Run by suite (groups baked in)
```bash
# Smoke only
mvn -Dsurefire.suiteXmlFiles=src/test/resources/testng-smoke.xml test
# Regression (excludes smoke)
mvn -Dsurefire.suiteXmlFiles=src/test/resources/testng-regression.xml test
# Parallel (both groups)
mvn -Dsurefire.suiteXmlFiles=src/test/resources/testng-parallel.xml test
```

### Run by groups from CLI
```bash
# Only smoke tests
mvn -Dgroups=smoke test
# Exclude smoke
mvn -DexcludedGroups=smoke test
```

## Allure environment
- `src/test/resources/allure/environment.properties` will show up under **Environment** in Allure.

## GitHub Actions
A ready-to-use workflow is at `.github/workflows/ci.yml`.
It runs the smoke suite headless and uploads:
- `surefire-reports/`
- `allure-results/`
- Generated Allure HTML (`target/site/allure-maven-plugin`)
