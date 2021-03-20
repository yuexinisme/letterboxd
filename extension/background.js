	// Copyright 2018 The Chromium Authors. All rights reserved.
	// Use of this source code is governed by a BSD-style license that can be
	// found in the LICENSE file.

	var els = document.getElementsByClassName("load-more-activity ajax-action");
	for (var i = 0; i < els.length; i++) {
		var el = els[i];
		el.onclick = function() {
			setTimeout(refresh, 6000);
		}
	}

	window.onload = function() {
		setTimeout(refresh, 6000);
	}
	function refresh(){ 
		console.log("refresh")
		var url= window.location.href;
		console.log("url:" + url);
		if (url != "https://letterboxd.com/activity/" && !url.startsWith("https://letterboxd.com/nickofdasouf/friends/film/")
			&& !url.startsWith("https://letterboxd.com/nickofdasouf/following/page/"))
			return;
		console.log("matched")
		if (url.startsWith("https://letterboxd.com/nickofdasouf/following/page/")) {
			h3s = document.getElementsByTagName('h3');
			let sz = h3s.length;

			console.log("follower size:" + sz)
			for (var i = 0; i < sz; i++) {
				var strong = h3s[i];
				var name = strong.innerText;
				if (name.includes(":"))
					continue;
				count_num(name, strong);
			}
		} else {
			var size = 0;
			var strongs = [];

			strongs = document.getElementsByTagName('strong');
			size = strongs.length;

			console.log("other size:" + size)
			for (var i = 0; i < strongs.length; i++) {
				var strong = strongs[i];
				var name = strong.innerText;
				if (name.includes(":"))
					continue;
				count_num(name, strong);
			}
		}

	}

	function count_num(name, strong) {
	    // XMLHttpRequest对象用于在后台与服务器交换数据   
	    var xhr = new XMLHttpRequest();    
	    var url = "http://localhost:8090/get?name=" + encodeURI(name);        
	    xhr.open('GET', url, true);
	    xhr.onreadystatechange = function() {
	      // readyState == 4说明请求已完成
	      if (xhr.readyState == 4 && xhr.status == 200 || xhr.status == 304) { 
	        // 从服务器获得数据 
	        console.log(name + ":" + xhr.responseText)
	        var newName = name + ":" + xhr.responseText;
	        console.log("new:" + newName);
	        strong.innerText = newName;
	    }
	};
	xhr.send();
}