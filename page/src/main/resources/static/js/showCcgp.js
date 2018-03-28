var stockData;


function showCcgp(tabid) {
    // console.log(tabid);
    var rootDiv = $('<div>', {'id': 'ccgp' + tabid, 'class': 'l-tab-content-item'});
    var lhbDiv = $('<div>', {'class': 'l-tab-content-item'});
    var clearDiv = $('<div>', {'class': 'l-clear'});
    var maingridDiv = $('<div>', {'id': 'maingridCcgp'});
    lhbDiv.prepend(clearDiv);
    lhbDiv.append(maingridDiv);
    rootDiv.append(lhbDiv);
    $.ajax({
        type: 'GET',
        async:false,
        contentType: 'application/json',
        url: '/spmm/stocks',
        success: function (data) {
            stockData = JSON.parse(data);
            // console.log(stockData);
        }
    });
    // console.log(stockData);
    $("div[tabid='" + tabid + "']").append(rootDiv);

    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/ccgp',
        success: function (data) {
            // console.log(data);
            var resultData = JSON.parse(data);
            // console.log(resultData);
            $("#maingridCcgp").ligerGrid({
                height: '95%',
                columns: [
                    {
                        display: '股票代码',
                        name: 'stockcode', minWidth: 90, editor: {
                            type: 'popup', valueField: 'stockcode', textField: 'stockcode',readOnly:false,
                            grid: {
                                data: stockData,
                                columns: [
                                    { display: '股票代码', name: 'stockcode', align: 'right', width: 150, minWidth: 120 },
                                    { display: '股票名称', name: 'stockname',align: 'center', minWidth: 120, width: 150 },
                                    { display: '行业', name: 'hangye',align: 'center', minWidth: 120, width: 150 },
                                    { display: '标签', name: 'tag',align: 'center', minWidth: 120, width: 150 }
                                ], usePager: true, isScroll: true, checkbox: false,

                                width: '95%'
                            },
                            condition: {
                                prefixID: 'condtion_',
                                fields: [
                                    {name: 'stockcode', type: 'text', label: '股票代码', width: 200}
                                ]
                            }, onSelected: function (e) {
                                // console.log(e);
                                var manager = $("#maingridCcgp").ligerGetGridManager();
                                var row = manager.getSelected();
                                $.ajax({
                                    type: 'GET',
                                    contentType: 'application/json',
                                    url: '/ccgp/getStock?stockcode='+e.data[0].stockcode,
                                    success: function (data) {
                                        console.log(data);
                                        manager.updateCell('stockcode', e.data[0].stockcode, row);
                                        manager.updateCell('stockname', e.data[0].stockname, row);
                                        manager.updateCell('followPrice', data, row);
                                        manager.updateCell('ramarks', e.data[0].ramarks, row);
                                    }
                                });

                            },
                            searchClick: function (e) {
                                // alert("这里可以根据搜索规则来搜索（下面的代码已经本地搜索),搜索规则:" + liger.toJSON(e.rules));
                                e.grid.loadData($.ligerFilter.getFilterFunction(e.rules));
                            }
                        }
                    },
                    {
                        display: '股票名称',
                        name: 'stockname', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '关注日期',
                        name: 'followDate', minWidth: 90, type: 'date', editor: {type: 'date'}
                    },
                    {
                        display: '关注点价格',
                        name: 'followPrice', minWidth: 90, editor: {type: 'float'},
                        render:function (item) {
                            // console.log(item.jiage);
                            return Math.abs(parseFloat(item.followPrice));
                        }
                    },
                    {
                        display: '现价',
                        name: 'nowPrice', minWidth: 90, editor: {type: 'float'},
                        render:function (item) {
                            // console.log(item.jiage);
                            return Math.abs(parseFloat(item.nowPrice));
                        }
                    },
                    {
                        display: '涨跌幅度',
                        name: 'zdl', minWidth: 90, editor: {type: 'text'},
                        render:function (item) {
                            // console.log(item.jiage);
                            if(item.zdl==undefined){
                                return "";
                            }else
                                return item.zdl+"%";
                        }
                    },
                    {
                        display: '概念',
                        name: 'ramarks', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '已关注天数',
                        name: 'dateDiff', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '',
                        name: 'id', width: 10, hide: true
                    }
                ], data: resultData, pageSize: 25, rownumbers: true, enabledEdit: true,
                usePager:false,isScroll: true,
                toolbar: {
                    items: [
                        {text: '增加', click: addCcgpRow, icon: 'add'},
                        {line: true},
                        {text: '删除', click: delCcgpRow, icon: 'delete'},
                        {line: true},
                        {text: '保存', click: saveCcgp, icon: 'save'}
                    ]
                }
            });
        }
    });
}

function ccgpFresh() {
    // console.log("刷新");
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/ccgp',
        success: function (data) {
            var resultData = JSON.parse(data);
            var manager = $("#maingridCcgp").ligerGetGridManager();
            manager.loadData(resultData);
            // console.log("刷新完成");
        }
    });
}

function delCcgpRow() {
    // var manager = $("#maingridCcgp").ligerGetGridManager();
    // var selected = manager.getSelected();
    // console.log(selected);

    var manager = $("#maingridCcgp").ligerGetGridManager();
    var row = manager.getSelectedRow();
    var recId = row['id'];
    // alert(recId);
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/ccgp/delete',
        data: {'recId': recId},
        success: function (data) {
            if(data != "删除失败")    {
                manager.deleteSelectedRow();
            }
            alert(data);
        }
    });
}
function addCcgpRow() {
    var manager = $("#maingridCcgp").ligerGetGridManager();
    var row = manager.getRow(0);
    var rq = new Date().format("yyyy-MM-dd");
    // console.log(rq);
    //参数1:rowdata(非必填)
    //参数2:插入的位置 Row Data
    //参数3:之前或者之后(非必填)
    manager.addRow({
        stockcode: "",
        stockname: "",
        followDate: rq,
        followPrice: "",
        nowPrice: "",
        zdl: "",
        ramarks: "",
        dateDiff: ""
    }, row, 1);
}
function saveCcgp() {
    $("#pageloading").show();
    var manager = $("#maingridCcgp").ligerGetGridManager();
    var row = manager.getSelectedRow();
    // alert(JSON.stringify(row));
    try {
        row["followDate"] = row["followDate"].format("yyyy-MM-dd");
    } catch (e) {
        console.log(e);
        row["followDate"] = row["followDate"];
    }
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: '/ccgp/save',
        data: JSON.stringify(row),
        success: function (data) {
            ccgpFresh();
            alert(data);
        }
    });
    $("#pageloading").hide();
}
function editCcgp(params) {
    var manager = $("#maingridCcgp").ligerGetGridManager();
    try {
        var data = manager.getData();
        var param = JSON.parse(params);
        var stockcode = param["stockcode"];
        for (var rowData in data) {
            var rowStockcode = data[rowData]["stockcode"];
            if (stockcode == rowStockcode) {
                var selected = manager.getRow(rowData);
                // console.log(selected);
                manager.updateRow(selected,{
                    stockcode:param["stockcode"],
                    stockname:param["stockname"],
                    followDate:param["followDate"],
                    followPrice:param["followPrice"],
                    nowPrice : param["nowPrice"],
                    zdl : param["zdl"],
                    ramarks : param["ramarks"],
                    dateDiff : param["dateDiff"]
                });
            }
        }
    }catch (e){
        console.log(e);
    }
}