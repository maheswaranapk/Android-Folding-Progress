# **Android-Folding-Progress**
This is a Simple Android Folding Progress Indicator.

**Design Inspired from [Google loading - Material Up!](http://www.materialup.com/posts/google-loading)**

# **How To use**
Copy **FadeCircleLoading.java** to the project.

In Layout xml,
```xml
            <com.scriptedpapers.foldingprogress.FadeCircleLoading
                    android:id="@+id/progressView"
                    android:layout_width="200dp"
                    android:layout_height="200dp"
                    android:layout_centerHorizontal="true"
                    android:layout_margin="25dp" />
```

In View,
```
            progressView.startAnimation();
```

# **Output**
![alt tag](https://github.com/maheswaranapk/Android-Folding-Progress/blob/master/demo/demo.gif)
