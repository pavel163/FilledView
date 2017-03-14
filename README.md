# FilledView

![Showcase](https://github.com/pavel163/FilledView/blob/master/files/gif1.gif)
![Showcase](https://github.com/pavel163/FilledView/blob/master/files/gif2.gif)

## Gradle

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

```gradle
dependencies {
    compile 'com.github.pavel163:FilledView:1.0.1'
}
```

## How to use

```xml
<com.ebr163.view.FilledView
        android:id="@+id/filled_view"
        android:layout_width="300dp"
        android:layout_height="150dp"
        app:border_show="true"
        app:fill_color="@color/colorAccent"
        app:radius="50dp"
        app:start_mode="left"
        app:text="TEST"
        app:textSize="24sp" />
```

Additonal optional attributes:

* fill_color - color
* border_show - boolean
* radius - dimension (corner radius)
* start_mode - left, top, right or bottom
* text - string
* textSize - dimension

Other configurable APIs:

* `public void setProgress(float percent)`, set progress (`percent >= 0f && percent <= 1f`)
* `public void setFillColor(int color)`
* `public void setText(String text)`
* `public void showBorder(boolean value)`
* `public void setTextSize(int textSize)`
* `public void setRadius(int radius)`
* `public void setStartMode(StartMode startPosition)`

## License
MIT
