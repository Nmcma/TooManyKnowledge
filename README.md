# Too Many Knowledge

## 前言
这个轻量级应用旨在帮助使用者记录知识点。其实际维护了一个Room数据库。通过Room存储了相关内容。它的特点在于能够从标题，关键词和内容三方面检索你需要的部分。当然，由于最开始没有做发布方面的准备，所以应用本身是十分简陋的。

## 概念

### 项
项是用于在数据库中存储的类。一个项包括名称，关键词和描述。

### 名称
一个内容的名称。其中，名称作为主键使用，因此是不能重复的。当修改名称时，旧名称项不会被删除，因此需要手动删除旧项。

### 关键词
对于项来说，其具有的关键词实际上是一个String数组，储存时会整体转化为一个String。关键词在一个项中是唯一的。

### 描述
一个项的细节内容，是一个简单String。

### 上下文
上下文是一个关键词的组合，其名称也是唯一的，你可以保存这个上下文，下次加载时可以将其所包含的所有关键词载入搜索约束中。

### 设置
设置仅是用来改变应用主题配色的，目前还没有其它功能。

## 如何使用

### 主界面

![image](https://github.com/Nmcma/TooManyKnowledge/blob/master/Demo/MainPageGuide.png)
<br/>
### 关键词匹配约束
对于大量搜索结果，可使用关键词约束来筛选结果。对于一个匹配的项，只有其关键词中具有所有选定的约束关键词时，才会显示在下面的搜索结果中。

![Add_Keyword_guide.gif](https://github.com/Nmcma/TooManyKnowledge/blob/master/Demo/Add_Keyword_guide.gif)
<br/>
特别地，您可添加以`^`开始的关键词，这样应用会筛选以该关键词为开头的匹配项。同样，以`$`结尾的关键词会筛选该关键词为结尾的匹配项。但是，该应用不支持其它任何形式的正则表达式。

![Use_Simple_Regex.gif](https://github.com/Nmcma/TooManyKnowledge/blob/master/Demo/Use_Simple_Regex.gif)
<br/>
您可以通过点击约束关键词来切换其可用状态。
### 新建项
新建项时，搜索框中的名称，关键词匹配约束中激活的关键词会被复制到表单中，但是请注意，包含正则表达式的关键词不能，也不应该包含在项的关键词中。因此，您需要手动删除那些关键词。

![Add_New_Item.gif](https://github.com/Nmcma/TooManyKnowledge/blob/master/Demo/Add_New_Item.gif)
