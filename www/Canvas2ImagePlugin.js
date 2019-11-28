//
//  Canvas2ImagePlugin.js
//  Canvas2ImagePlugin PhoneGap/Cordova plugin
//
//  Created by Tommy-Carlos Williams on 29/03/12.
//  Copyright (c) 2012 Tommy-Carlos Williams. All rights reserved.
//  MIT Licensed
//

module.exports = {
	saveImageDataToLibrary: function(
		successCallback,
		failureCallback,
		canvasId,
		format
	) {
		format = typeof format !== "undefined" ? format : "png"; // default value

		// successCallback required
		if (typeof successCallback != "function") {
			console.log(
				"Canvas2ImagePlugin Error: successCallback is not a function"
			);
		} else if (typeof failureCallback != "function") {
			console.log(
				"Canvas2ImagePlugin Error: failureCallback is not a function"
			);
		} else {
			var canvas =
				typeof canvasId === "string"
					? document.getElementById(canvasId)
					: canvasId;
			var imageData = canvas
				.toDataURL("image/" + format, 1)
				.replace("data:image/" + format + ";base64,", "");
			return cordova.exec(
				successCallback,
				failureCallback,
				"Canvas2ImagePlugin",
				"saveImageDataToLibrary",
				[imageData, format]
			);
		}
	}
};
