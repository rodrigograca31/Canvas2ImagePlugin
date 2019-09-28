Canvas2ImagePlugin
============

### NOTE: I'm gonna try my best to keep this plugin alive moving foward. I've incorporated two important PRs so far, will be accepting more if submitted. (I can read Java and test on Android but I can't code Java...)

This plugin allows you to save the contents of an HTML canvas tag to the iOS Photo Library, Android Gallery or WindowsPhone 8 Photo Album from your app.

See an example project using it here: [https://github.com/devgeeks/Canvas2ImageDemo](https://github.com/devgeeks/Canvas2ImageDemo) - note: this app does not work in wp8.

Installation
------------

### For Cordova 3.0.x:

1. To add this plugin just type: `cordova plugin add https://github.com/rodrigograca31/Canvas2ImagePlugin`
2. To remove this plugin type: `cordova plugin rm https://github.com/rodrigograca31/Canvas2ImagePlugin`

### NOTE: For older versions of Cordova (You will probably have to use tag 0.2.0)

For iOS, you will need to add the following to your config.xml file within `<platform name="ios">`:

```
<edit-config file="*-Info.plist" mode="merge" target="NSPhotoLibraryAddUsageDescription">
    <string>Description goes here.</string>
</edit-config>
```

Usage:
------

Call the `window.canvas2ImagePlugin.saveImageDataToLibrary()` method using success and error callbacks and the id attribute or the element object of the canvas to save:

### Example
```html
<canvas id="myCanvas" width="165px" height="145px"></canvas>
```

```javascript
function onDeviceReady()
{
	window.canvas2ImagePlugin.saveImageDataToLibrary(
		function(msg){
			console.log(msg);
		},
		function(err){
			console.log(err);
		},
		document.getElementById('myCanvas'),
		"jpeg" // format is optional, defaults to 'png'
	);
}
```

## License

The MIT License

Copyright (c) 2011 Tommy-Carlos Williams (http://github.com/devgeeks)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
