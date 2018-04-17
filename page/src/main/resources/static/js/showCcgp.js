var stockData;
var win2;
var resultData;
$.ajax({
    type: 'GET',
    async:false,
    contentType: 'application/json',
    url: '/spmm/stocks',
    success: function (data) {
        if(data !='') {
            stockData = JSON.parse(data);
            console.log(stockData);
        }
    }
});
function showCcgp(tabid) {
    // console.log(tabid);
    win2 = $.ligerDialog.open({ title:"走势图",url:"", width: 1700,height:950,modal:true });
    win2.hide();
    var rootDiv = $('<div>', {'id': 'ccgp' + tabid, 'class': 'l-tab-content-item'});
    var secondSpan = $('<span>',{'id':'now1','style':'color: #0066ff;'});
    rootDiv.append(secondSpan);
    secondSpan.html(new Date().format("yyyy年MM月dd日 hh:mm:ss"));
    var lhbDiv = $('<div>', {'class': 'l-tab-content-item'});
    var clearDiv = $('<div>', {'class': 'l-clear'});
    var maingridDiv = $('<div>', {'id': 'maingridCcgp'});
    lhbDiv.prepend(clearDiv);
    lhbDiv.append(maingridDiv);
    rootDiv.append(lhbDiv);




    // console.log(stockData);
    $("div[tabid='" + tabid + "']").append(rootDiv);

    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/ccgp/index',
        success: function (data) {
            // console.log(data);
            if (data != '') {
                resultData = JSON.parse(data);
                // console.log(resultData);
                $("#maingridCcgp").ligerGrid({
                    height: '95%',
                    columns: [
                        {
                            display: '股票代码',
                            name: 'stockcode', minWidth: 90, editor: {
                                type: 'popup', valueField: 'stockcode', textField: 'stockcode', readOnly: false,
                                grid: {
                                    data: stockData,
                                    columns: [
                                        {display: '股票代码', name: 'stockcode', align: 'right', width: 150, minWidth: 120},
                                        {
                                            display: '股票名称',
                                            name: 'stockname',
                                            align: 'center',
                                            minWidth: 120,
                                            width: 150
                                        },
                                        {display: '行业', name: 'hangye', align: 'center', minWidth: 120, width: 150},
                                        {display: '标签', name: 'tag', align: 'center', minWidth: 120, width: 150}
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
                                        url: '/ccgp/getStock?stockcode=' + e.data[0].stockcode,
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
                            render: function (item) {
                                // console.log(item.jiage);
                                return Math.abs(parseFloat(item.followPrice));
                            }
                        },
                        {
                            display: '最新价',
                            name: 'nowPrice', minWidth: 90, editor: {type: 'float'},
                            render: function (item) {
                                // console.log(item.jiage);
                                var diffPrice = parseFloat(item.nowPrice) - parseFloat(item.followPrice);
                                if (diffPrice > 0) {
                                    return '<span style="color:red">' + parseFloat(item.nowPrice).toFixed(2) + '</span>';
                                } else {
                                    return '<span style="color:green">' + parseFloat(item.nowPrice).toFixed(2) + '</span>';
                                }
                            }
                        },
                        {
                            display: '涨跌幅度',
                            name: 'zdl', minWidth: 90, editor: {type: 'text'},
                            render: function (item) {
                                // console.log(item.jiage);
                                var followPrice = parseFloat(item.followPrice);
                                var nowPrice = parseFloat(item.nowPrice);
                                var diff = nowPrice - followPrice;
                                var zdz = (diff / followPrice) * 100;
                                // console.log(zdz);
                                if (diff > 0) {
                                    return '<span style="color:red">' + zdz.toFixed(2) + '%</span>';
                                } else {
                                    return '<span style="color:green">' + zdz.toFixed(2) + '%</span>';
                                }
                            }
                        },
                        {
                            display: '概念',
                            name: 'ramarks', minWidth: 200, editor: {type: 'text'}
                        },
                        {
                            display: '已关注天数',
                            name: 'dateDiff',  editor: {type: 'text'},
                            render: function (item) {
                                var date1 = new Date();
                                var fDate = item.followDate.split(" ");
                                var dateDiff = DateDiff(date1.format("yyyy-MM-dd"), fDate[0]);
                                // console.log(dateDiff)
                                return dateDiff;
                            }
                        },
                        {
                            display: '',
                            name: 'id', width: 10, hide: true
                        }
                    ], data: resultData, pageSize: 25, rownumbers: true, enabledEdit: true,
                    usePager: false, isScroll: true,
                    toolbar: {
                        items: [
                            {text: '增加', click: addCcgpRow, icon: 'add'},
                            {line: true},
                            {text: '删除', click: delCcgpRow, icon: 'delete'},
                            {line: true},
                            {text: '保存', click: saveCcgp, icon: 'save'},
                            {line: true},
                            {text: '走势图', click: showChart, icon: 'save'},
                            {line: true}
                        ]
                    }
                });
            }
        }
    });
}
function DateDiff(d1,d2){
    var day = 24 * 60 * 60 *1000;
    try{
        var dateArr = d1.split("-");
        var checkDate = new Date();
        checkDate.setFullYear(dateArr[0], dateArr[1]-1, dateArr[2]);
        var checkTime = checkDate.getTime();

        var dateArr2 = d2.split("-");
        var checkDate2 = new Date();
        checkDate2.setFullYear(dateArr2[0], dateArr2[1]-1, dateArr2[2]);
        // console.log(checkDate2);
        var checkTime2 = checkDate2.getTime();

        var cha = (checkTime - checkTime2)/day;
        return cha;
    }catch(e){
        return false;
    }
}//end fun
function ccgpFresh() {
    // console.log("刷新");
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/ccgp/index',
        success: function (data) {
            resultData = JSON.parse(data);
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
        if(manager!=null) {
            var data = manager.getData();
            $("#now1").html(new Date().format("yyyy年MM月dd日 hh:mm:ss"));
            if ($("#now").length > 0) {
                $("#now").html(new Date().format("yyyy年MM月dd日 hh:mm:ss"));
            }
            var param = params;
            var stockcode = param["stockcode"];
            for (var rowData in data) {
                var rowStockcode = data[rowData]["stockcode"];
                if (stockcode == rowStockcode) {
                    var selected = manager.getRow(rowData);
                    // console.log(selected);
                    manager.updateRow(selected, {
                        stockcode: param["stockcode"],
                        stockname: param["stockname"],
                        followDate: param["followDate"],
                        followPrice: param["followPrice"],
                        nowPrice: param["nowPrice"],
                        zdl: param["zdl"],
                        ramarks: param["ramarks"],
                        dateDiff: param["dateDiff"]
                    });
                }
            }
        }
    }catch (e){
        console.log(e);
    }
}
function showChart() {
    if(win2) {
        win2.show();
        var rootDiv = $(".l-dialog-content.l-dialog-content-noimage");
        if($("#now").length== 0) {
        var firstSpan = $('<span>',{'id':'now','style':'color: #0066ff;'});

            rootDiv.append(firstSpan);
            firstSpan.html(new Date().format("yyyy年MM月dd日 hh:mm:ss"));

            var jqGridChart = $('<div>', {
                'id': 'jqGridChart',
                'style': 'float:left;padding-top:15px;padding-left:0px;margin: 0px; width: auto;position: relative; overflow: hidden;'
            });
            rootDiv.append(jqGridChart);
            var shDiv = $('<div>', {'id': 'div_000001', 'style': 'float:left;padding: 0px;margin: 0px; width: auto'});
            var shImage = $("<image id='chart_000001' class='imgMinChart' src=http://image.sinajs.cn/newchart/min/n/sh000001.gif/>");
            shDiv.addClass('divChart');
            shDiv.html(shImage);
            jqGridChart.append(shDiv);
            var szDiv = $('<div>', {'id': 'div_399001', 'style': 'float:left;padding: 0px;margin: 0px; width: auto'});
            var szImage = $("<image id='chart_399001' class='imgMinChart' src=http://image.sinajs.cn/newchart/min/n/sz399001.gif/>");
            szDiv.addClass('divChart');
            szDiv.html(szImage);
            jqGridChart.append(szDiv);
            var cyDiv = $('<div>', {'id': 'div_399006', 'style': 'float:left;padding: 0px;margin: 0px; width: auto'});
            var cyImage = $("<image id='chart_399006' class='imgMinChart' src=http://image.sinajs.cn/newchart/min/n/sz399006.gif/>");
            cyDiv.addClass('divChart');
            cyDiv.html(cyImage);
            jqGridChart.append(cyDiv);
            //
            var manager = $("#maingridCcgp").ligerGetGridManager();
            var data = manager.getData();
            for (var rowData in data) {
                var rowStockcode = data[rowData]["stockcode"];
                var chartUrl = "http://image.sinajs.cn/newchart/min/n/$code$.gif";
                if (rowStockcode.startsWith("00") || rowStockcode.startsWith("30")) {
                    code = "sz" + rowStockcode;
                } else {
                    code = "sh" + rowStockcode;
                }
                chartUrl = chartUrl.replace("$code$", code);
                var objNewDiv = $('<div>', {
                    'id': 'div_' + code,
                    'style': 'float:left;padding: 0px;margin: 0px; width: auto'
                });
                objNewDiv.addClass('divChart');
                var image = $("<image id='chart_" + code + "' class='imgMinChart' src=" + chartUrl + "/>");
                objNewDiv.html(image);
                //console.log(objNewDiv);
                jqGridChart.append(objNewDiv);
            }
        }
    }
}
window.setInterval(updateChart,1000*60*2);
function  updateChart() {
    var imgMinCharts = $(".imgMinChart");
    // console.log(imgMinCharts.length);
    for(var i=0;i<imgMinCharts.length;i++){
        var imgMinChart = imgMinCharts[i];
        var path = imgMinChart.src+"?p="+Math.random();
        $(imgMinChart).attr('src',path);
    }
}