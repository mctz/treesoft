function ECDSA() {
    _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    this.encode = function (a) {
        var b = "", c = 0;
        for (a = _utf8_encode(a); c < a.length;) {
            var d = a.charCodeAt(c++);
            var e = a.charCodeAt(c++);
            var f = a.charCodeAt(c++);
            var g = d >> 2;
            d = (d & 3) << 4 | e >> 4;
            var h = (e & 15) << 2 | f >> 6;
            var k = f & 63;
            isNaN(e) ? h = k = 64 : isNaN(f) && (k = 64);
            b = b + _keyStr.charAt(g) + _keyStr.charAt(d) + _keyStr.charAt(h) + _keyStr.charAt(k)
        }
        return b
    };
    this.decode = function (a) {
        var b = "", c = 0;
        for (a = a.replace(/[^A-Za-z0-9\+\/=]/g, ""); c < a.length;) {
            var d =
                _keyStr.indexOf(a.charAt(c++));
            var e = _keyStr.indexOf(a.charAt(c++));
            var f = _keyStr.indexOf(a.charAt(c++));
            var g = _keyStr.indexOf(a.charAt(c++));
            d = d << 2 | e >> 4;
            e = (e & 15) << 4 | f >> 2;
            var h = (f & 3) << 6 | g;
            b += String.fromCharCode(d);
            64 != f && (b += String.fromCharCode(e));
            64 != g && (b += String.fromCharCode(h))
        }
        return b = _utf8_decode(b)
    };
    _utf8_encode = function (a) {
        a = a.replace(/\r\n/g, "\n");
        for (var b = "", c = 0; c < a.length; c++) {
            var d = a.charCodeAt(c);
            128 > d ? b += String.fromCharCode(d) : (127 < d && 2048 > d ? b += String.fromCharCode(d >> 6 | 192) : (b += String.fromCharCode(d >>
                12 | 224), b += String.fromCharCode(d >> 6 & 63 | 128)), b += String.fromCharCode(d & 63 | 128))
        }
        return b
    };
    _utf8_decode = function (a) {
        var b = "", c = 0;
        for (c1 = c2 = 0; c < a.length;) {
            var d = a.charCodeAt(c);
            128 > d ? (b += String.fromCharCode(d), c++) : 191 < d && 224 > d ? (c2 = a.charCodeAt(c + 1), b += String.fromCharCode((d & 31) << 6 | c2 & 63), c += 2) : (c2 = a.charCodeAt(c + 1), c3 = a.charCodeAt(c + 2), b += String.fromCharCode((d & 15) << 12 | (c2 & 63) << 6 | c3 & 63), c += 3)
        }
        return b
    }
}

var Ecdsa = new ECDSA;

function refreshCaptcha() {
    document.getElementById("img_captcha").src = baseUrl + "/static/images/securityCode.jpg?t=" + Math.random()
}

function check() {
    return "" == document.getElementById("username1").value ? (document.getElementById("login_main_errortip").innerHTML = "\u8bf7\u8f93\u5165\u7528\u6237\u540d!", !1) : "" == document.getElementById("password1").value ? (document.getElementById("login_main_errortip").innerHTML = "\u8bf7\u8f93\u5165\u5bc6\u7801!", !1) : "" == document.getElementById("captcha").value ? (document.getElementById("login_main_errortip").innerHTML = "\u8bf7\u8f93\u5165\u9a8c\u8bc1\u7801!", !1) : 15 < document.getElementById("username1").value.length ?
        (document.getElementById("login_main_errortip").innerHTML = "\u8bf7\u8f93\u5165\u6709\u6548\u7684\u7528\u6237\u540d!", !1) : !0
}

function KeyDown() {
    13 == event.keyCode && (event.returnValue = !1, event.cancel = !0, loginSystem())
}

function loginSystem() {
    if (!check()) return check();
    ajax({
        url: baseUrl + "/treesoft/loginVaildate",
        type: "POST",
        data: {
            username1: Ecdsa.encode(document.getElementById("username1").value),
            password1: Ecdsa.encode(document.getElementById("password1").value),
            captcha: document.getElementById("captcha").value
        },
        success: function (a) {
            a = JSON.parse(a);
            "success" == a.status ? (document.getElementById("login_main_errortip").innerHTML = "\u767b\u5f55\u6210\u529f\uff0c\u8df3\u8f6c\u4e2d!", window.setTimeout(function () {
                window.location.href =
                    baseUrl + "/treesoft/index"
            }, 100)) : (document.getElementById("login_main_errortip").innerHTML = a.message, refreshCaptcha())
        }
    })
}

function ajax(a) {
    var b = {
        type: a.type || "GET",
        url: a.url || "",
        async: a.async || "true",
        data: a.data || null,
        dataType: a.dataType || "text",
        contentType: a.contentType || "application/x-www-form-urlencoded",
        beforeSend: a.beforeSend || function () {
        },
        success: a.success || function () {
        },
        error: a.error || function () {
        }
    };
    b.beforeSend();
    var c = createxmlHttpRequest();
    c.open(b.type, b.url, b.async);
    c.setRequestHeader("Content-Type", b.contentType);
    c.send(convertData(b.data));
    c.onreadystatechange = function () {
        4 == c.readyState && (200 == c.status ?
            b.success(c.response) : b.error())
    }
}

function createxmlHttpRequest() {
    if (window.ActiveXObject) return new ActiveXObject("Microsoft.XMLHTTP");
    if (window.XMLHttpRequest) return new XMLHttpRequest
}

function convertData(a) {
    if ("object" === typeof a) {
        var b = "", c;
        for (c in a) b += c + "=" + a[c] + "&";
        return b = b.substring(0, b.length - 1)
    }
    return a
};
