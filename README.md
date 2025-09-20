# Sonar 自定义规则

## 开发步骤

### 一、开发规则

在  `java.sonar.rules` 下新增 `rule` 作为规则运行的代码

### 二、创建规则元数据

在`resources`目录下创建`rules/java`目录，并在里面创建 `x.html` 、`x.json` （x 为开发规则中`rule`绑定的`key`）

### 三、规则分类

同意放到 `RulesList ` 中

### 四、定义编码规则

到此为止，上面的过程创建了规则运行代码、创建了规则的元数据、将规则进行了分类存储，那么接下来便要将这些规则集成到sonar中去进行应用，即放入 `RulesDefinition` 中

### 五、注册规则

因为定义的规则依赖于`Java API`的`SonarSource Analyzer`，所以还需要告诉父插件必须检索一些新规则

### 六、定义插件

插件将扩展注入`SonarQube`的入口点，即`Plugin`



## Sonar 配置

### 一、打包发布

`maven`打包发布后放入 `sonar qube` 的 `plugin` 目录下，如 `sonarqube\extensions\plugins`

### 二、激活配置

在 `sonar` 的 `Quality Profiles` 中将新增的规则激活（可以复制原来的 `java` 的 `profile`，并将自定义规则加入到 `profile` 中，再将该 `profile` 设为 `default`）