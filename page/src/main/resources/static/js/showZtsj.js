var $grid;

function showZtsj(tabid) {
    // console.log(tabid);
    var rootDiv = $('<div>', {'id': 'ztsj' + tabid, 'class': 'l-tab-content-item'});
    var lhbDiv = $('<div>', {'class': 'l-tab-content-item'});
    var clearDiv = $('<div>', {'class': 'l-clear'});
    var maingridDiv = $('<div>', {'id': 'maingrid'});
    lhbDiv.prepend(clearDiv);
    lhbDiv.append(maingridDiv);
    rootDiv.append(lhbDiv);
    $("div[tabid='"+tabid+"']").append(rootDiv);
    // console.log($("div[tabid='"+tabid+"']"));
    // var gnData;
    // $.ajax({
    //     type: 'GET',
    //     contentType: 'application/json',
    //     url: '/ztgn',
    //     success: function (data) {
    //         gnData = JSON.parse(data);
    //         // console.log(gnData);
    //     }
    // });

    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/ztsj/index',
        success: function (data) {
            var resultData = JSON.parse(data);
            // console.log(resultData);
            $grid = $("#maingrid").ligerGrid({
                height: '95%',
                columns: [
                    {
                        display: '日期',
                        name: 'rq', align: 'left', width: 90, minWidth: 60, type: 'date', editor: {type: 'date'}
                    },
                    {
                        display: '每日涨停数量',
                        name: 'mrztgs', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '非一字板个数',
                        name: 'fyzbgs', minWidth: 90, editor: {type: 'int'}
                    },
                    {
                        display: '10点前涨停个数',
                        name: 'dqztgs', minWidth: 90, editor: {type: 'int'}
                    },
                    {
                        display: '涨停最多概念',
                        name: 'ztzdgn', minWidth: 90, textField: 'ztzdgn',
                        editor: {
                            type: 'popup', valueField: 'ztzdgn', textField: 'ztzdgn',readOnly:false,
                            grid: {
                                columns: [
                                    {display: '涨停最多概念', name: 'ztzdgn', width: 200},
                                    {display: '累计', name: 'COUNTS', width: 200}
                                ], usePager: true, isScroll: true, checkbox: false,
                                url: "/ztsj/ztgn",
                                width: '95%'
                            },
                            condition: {
                                prefixID: 'condtion_',
                                fields: [
                                    {name: 'ztzdgn', type: 'text', label: '涨停最多概念', width: 200}
                                ]
                            }, onSelected: f_onSelected,
                            searchClick: function (e) {
                                // alert("这里可以根据搜索规则来搜索（下面的代码已经本地搜索),搜索规则:" + liger.toJSON(e.rules));
                                e.grid.loadData($.ligerFilter.getFilterFunction(e.rules));
                            }
                        }
                    },
                    {
                        display: '涨停最多个数',
                        name: 'ztzdgs', minWidth: 90, editor: {type: 'int'}
                    },
                    {
                        display: '打板次日高开率',
                        name: 'dbcrgkl', minWidth: 90, editor: {type: 'float'},render:function (item) {
                            try {
                                var indexOf = item.dbcrgkl.indexOf("%");
                            } catch (e) {
                                // console.log(e);
                                indexOf = -1;
                            }
                            if (indexOf == -1) {
                                // console.log(item.spcgl);
                                if (parseFloat(item.dbcrgkl) > 1) {
                                    var bzl = parseFloat(item.dbcrgkl) / 100;
                                    item.dbcrgkl = bzl;
                                    return item.dbcrgkl;
                                }else{
                                    return item.dbcrgkl;
                                }
                            } else {
                                return item.dbcrgkl;
                            }

                        }
                    },
                    {
                        display: '收盘成功率',
                        name: 'spcgl', minWidth: 90, editor: {type: 'float'},render:function (item) {
                            try {
                                var indexOf = item.spcgl.indexOf("%");
                            } catch (e) {
                                // console.log(e);
                                indexOf = -1;
                            }
                            if (indexOf == -1) {
                                // console.log(item.spcgl);
                                if (parseFloat(item.spcgl) > 1) {
                                    var bzl = parseFloat(item.spcgl) / 100;
                                    item.spcgl = bzl;
                                    return item.spcgl;
                                }else{
                                    return item.spcgl;
                                }
                            } else {
                                return item.spcgl;
                            }

                        }
                    },
                    {
                        display: '10点前涨停高开率',
                        name: 'dqztgkl', minWidth: 90, editor: {type: 'float'},render:function (item) {
                            try {
                                var indexOf = item.dqztgkl.indexOf("%");
                            } catch (e) {
                                // console.log(e);
                                indexOf = -1;
                            }
                            if (indexOf == -1) {
                                // console.log(item.dqztgkl);
                                if (parseFloat(item.dqztgkl) > 1) {
                                    var bzl = parseFloat(item.dqztgkl) / 100;
                                    item.dqztgkl = bzl;
                                    return item.dqztgkl;
                                }else{
                                    return item.dqztgkl;
                                }
                            } else {
                                return item.dqztgkl;
                            }

                        }
                    },
                    {
                        display: '被砸数量',
                        name: 'bzsl', minWidth: 90, editor: {type: 'int'}
                    },
                    {
                        display: '被砸率',
                        name: 'bzl', editor: {type: 'float'},render:function (item) {
                            try {
                                var indexOf = item.bzl.indexOf("%");
                            } catch (e) {
                                // console.log(e);
                                indexOf = -1;
                            }
                                if (indexOf == -1) {
                                    // console.log(item.bzl);
                                    if (parseFloat(item.bzl) > 1) {
                                        var bzl = parseFloat(item.bzl) / 100;
                                        item.bzl = bzl;
                                        return item.bzl;
                                    }else{
                                        return item.bzl;
                                    }
                                } else {
                                    return item.bzl;
                                }

                        }
                    },
                    {
                        display: '',
                        name: 'recid', width: 10, hide: true
                    }
                ], data: resultData, pageSize: 25, rownumbers: true, enabledEdit: true, onReload: fresh,
                onEndEdit:function (item) {
                    // console.log(item);
                    if(item.column.name=='ztzdgn') {
                        item.record.ztzdgn = item.text;
                        item.record.bzl = item.record.bzl;
                        item.record.dqztgkl = item.record.dqztgkl;
                        item.record.spcgl = item.record.spcgl;
                        item.record.dbcrgkl = item.record.dbcrgkl;
                    }
                    if(item.column.name=='bzl') {
                        item.record.bzl = item.value;
                        item.record.ztzdgn = item.record.ztzdgn;
                        item.record.dqztgkl = item.record.dqztgkl ;
                        item.record.spcgl = item.record.spcgl;
                        item.record.dbcrgkl = item.record.dbcrgkl;
                    }
                    if(item.column.name=='dqztgkl') {
                        item.record.ztzdgn = item.record.ztzdgn;
                        item.record.bzl = item.record.bzl;
                        item.record.dqztgkl = item.value;
                        item.record.spcgl = item.record.spcgl;
                        item.record.dbcrgkl = item.record.dbcrgkl;
                    }
                    if(item.column.name=='spcgl') {
                        item.record.ztzdgn = item.record.ztzdgn;
                        item.record.bzl = item.record.bzl;
                        item.record.dqztgkl = item.record.dqztgkl;
                        item.record.spcgl = item.value;
                        item.record.dbcrgkl = item.record.dbcrgkl;
                    }
                    if(item.column.name=='dbcrgkl') {
                        item.record.ztzdgn = item.record.ztzdgn;
                        item.record.bzl = item.record.bzl;
                        item.record.dqztgkl = item.record.dqztgkl;
                        item.record.spcgl = item.record.spcgl;
                        item.record.dbcrgkl =item.value;
                    }
                },
                toolbar: {
                    items: [
                        {text: '增加', click: itemclick, icon: 'add'},
                        {line: true},
                        {text: '删除', click: itemclick, icon: 'delete'},
                        {line: true},
                        {text: '保存', click: itemclick, icon: 'save'}
                    ]
                }
            });
        }
    });
}

function fresh() {
    // console.log("刷新");
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/ztsj/index',
        success: function (data) {
            var resultData = JSON.parse(data);
            $grid.loadData(resultData);
            // console.log("刷新完成");
        }
    });
}

function itemclick(item) {

    if (item.text == '增加') {
        addNewRow();
    } else if (item.text == '删除') {
        deleteRow();
    } else if (item.text == '保存') {
        save();
    }

}

function deleteRow() {
    var manager = $("#maingrid").ligerGetGridManager();
    var row = manager.getSelectedRow();
    var recId = row['recid'];
    // alert(recId);
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/ztsj/delete',
        data: {'recId': recId},
        success: function (data) {
            alert(data);
            fresh();
        }
    });
    manager.deleteSelectedRow();
}

function addNewRow() {

    var manager = $("#maingrid").ligerGetGridManager();
    var row = manager.getRow(0);
    var rq = new Date().format("yyyy-MM-dd");
    console.log(rq);
    //参数1:rowdata(非必填)
    //参数2:插入的位置 Row Data
    //参数3:之前或者之后(非必填)
    manager.addRow({
        rq: rq,
        mrztgs: "",
        fyzbgs: "",
        dqztgs: "",
        ztzdgn: "",
        ztzdgs: "",
        dbcrgkl: "",
        spcgl: "",
        dqztgs: "",
        dqztgkl: "",
        bzsl: "",
        bzl: ""
    }, row, 1);
}

function getSelected() {
    var manager = $("#maingrid").ligerGetGridManager();
    var row = manager.getSelectedRow();
    if (!row) {
        alert('请选择行');
        return;
    }
    alert(JSON.stringify(row));
}

function getData() {
    var manager = $("#maingrid").ligerGetGridManager();
    var data = manager.getData();
    alert(JSON.stringify(data));
}

function save() {
    $("#pageloading").show();
    var manager = $("#maingrid").ligerGetGridManager();
    var row = manager.getSelectedRow();
    // alert(JSON.stringify(row));
    try {
        row["rq"] = row["rq"].format("yyyy-MM-dd");
        if(row["dbcrgkl"].indexOf("%")>-1){
            row["dbcrgkl"] = row["dbcrgkl"].replace("%","");
            row["dbcrgkl"] = parseFloat(row["dbcrgkl"]) / 100;
        }
        if(row["spcgl"].indexOf("%")>-1){
            row["spcgl"] = row["spcgl"].replace("%","");
            row["spcgl"] = parseFloat(row["spcgl"]) / 100;
        }
        if(row["dqztgkl"].indexOf("%")>-1){
            row["dqztgkl"] = row["dqztgkl"].replace("%","");
            row["dqztgkl"] = parseFloat(row["dqztgkl"]) / 100;
        }
        if(row["bzl"].indexOf("%")>-1){
            row["bzl"] = row["bzl"].replace("%","");
            row["bzl"] = parseFloat(row["bzl"]) / 100;
        }
    } catch (e) {
        console.log(e);
        row["rq"] = row["rq"];
    }
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: '/ztsj/save',
        data: JSON.stringify(row),
        success: function (data) {
            alert(data);
            fresh();
        }
    });
    $("#pageloading").hide();
}

// e.value  e.text  e.data
function f_onSelected(e) {
    if (!e.data || !e.data.length) return;

    var grid = liger.get("maingrid");

    var selected = e.data[0];
    grid.updateRow(grid.lastEditRow, {
        ztzdgn: selected.ztzdgn
    });

    var out = JSON.stringify(selected);
    $("#message").html('最后选择:' + out);
}

