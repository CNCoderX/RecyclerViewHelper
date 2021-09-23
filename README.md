## 简介
RecyclerViewHelper是基于android 原生库RecyclerView的功能扩展，扩展的功能包括：
* Item的点击/长按事件
* 添加Header/Footer View
* 滑动到底部加载更多（Load More）
* 可收缩/展开的两级列表（类似ExpandableListView）
* 非常简单的添加分割线和列表间距
* Item抽屉效果（![Swipe Layout](https://github.com/daimajia/AndroidSwipeLayout)）
* 拓展了多种适配器Adapter

#### 增加的适配器
* ![ArrayAdapter](https://raw.githubusercontent.com/CNCoderX/RecyclerViewHelper/master/library/src/main/java/com/cncoderx/recyclerviewhelper/adapter/ArrayAdapter.java)
* ![SimpleAdapter](https://raw.githubusercontent.com/CNCoderX/RecyclerViewHelper/master/library/src/main/java/com/cncoderx/recyclerviewhelper/adapter/SimpleAdapter.java)
* ![CursorAdapter](https://raw.githubusercontent.com/CNCoderX/RecyclerViewHelper/master/library/src/main/java/com/cncoderx/recyclerviewhelper/adapter/CursorAdapter.java)、![SimpleCursorAdapter](https://raw.githubusercontent.com/CNCoderX/RecyclerViewHelper/master/library/src/main/java/com/cncoderx/recyclerviewhelper/adapter/SimpleCursorAdapter.java)
* ![ObjectAdapter](https://raw.githubusercontent.com/CNCoderX/RecyclerViewHelper/master/library/src/main/java/com/cncoderx/recyclerviewhelper/adapter/ObjectAdapter.java)
* ![ExpandableAdapter](https://raw.githubusercontent.com/CNCoderX/RecyclerViewHelper/master/library/src/main/java/com/cncoderx/recyclerviewhelper/adapter/ExpandableAdapter.java)

## 添加依赖
```gradle
compile 'com.github.CNCoderX:RecyclerViewHelper:1.2.3'

// 如果需要添加抽屉功能，添加依赖：
compile "com.daimajia.swipelayout:library:1.2.0@aar"
```


