var newLineStyle = 'word-break:break-all;word-wrap:break-word;white-space:pre-wrap;';

function executeAjax(url, method, params, callback) {
    $.messager.progress({
        title: '温馨提醒',
        msg: '数据加载中，请稍候...'
    });
    $.ajax({
        url: url,
        type: method,
        data: params,
        success: function (resp) {
            callback(resp);
        },
        error: function (e) {
            $.messager.alert('系统提醒', '加载出错，请刷新重试!', 'error');
        },
        complete: function () {
            $.messager.progress('close');
        }
    });
}

function executeAjaxGet(url, callback) {
    return executeAjax(url, 'GET', {}, callback);
}

function executeAjaxPost(url, params, callback) {
    return executeAjax(url, 'POST', params, callback);
}

function formatDate(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();

    return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
}

Date.prototype.format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));

    return fmt;
}

function formatDate(date) {
    return date.format('yyyy-MM-dd');
}

function formatFullDate(date) {
    return date.format('yyyy-MM-dd hh:mm:ss');
}