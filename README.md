# vhook
virtualhook的demo plugin直接硬编码进框架 不使用dexclassloader的方式加载
demoapk 黑盒 ,附上源码便于理解
hook到方法后 使用socket跨进程通信 可以直接在hook点打开框架里面自定义的activity,
达到更简单的hook ui的效果  （正向编码ui）
例子(demo)中logo的点击事件是弹toast  内容为edittext中的内容
hook 后 如果edittext中的内容为6 点击logo 跳转到ListAppActivity中

待优化 
1.ui
2.activity栈问题














