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