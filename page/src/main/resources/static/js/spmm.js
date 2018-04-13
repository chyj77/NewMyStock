var $grid;
var win1;
// var ljData;
// var  stockData;
function spmm(tabid) {
    // console.log(tabid);
    win1 = $.ligerDialog.open({ title:"实盘买卖",url:"", width: 600,height:400,modal:true });
    win1.hide();
    var rootDiv = $('<div>', {'id': 'spmm' + tabid, 'class': 'l-tab-content-item'});
    var lhbDiv = $('<div>', {'class': 'l-tab-content-item'});
    var clearDiv = $('<div>', {'class': 'l-clear'});
    var maingridDiv = $('<div>', {'id': 'maingridSpmm'});
    lhbDiv.prepend(clearDiv);
    lhbDiv.append(maingridDiv);
    rootDiv.append(lhbDiv);

    $("div[tabid='" + tabid + "']").append(rootDiv);

    // $.ajax({
    //     type: 'GET',
    //     contentType: 'application/json',
    //     url: '/spmm/luoji',
    //     success: function (data) {
    //         ljData = JSON.parse(data);
    //         console.log(ljData);
    //     }
    // });
    // $.ajax({
    //     type: 'GET',
    //     contentType: 'application/json',
    //     url: '/spmm/stocks',
    //     success: function (data) {
    //         stockData = JSON.parse(data);
    //         console.log(stockData);
    //     }
    // });

    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/spmm/index',
        success: function (data) {
            // console.log(data);
            var resultData = JSON.parse(data);
            // console.log(resultData);
            $grid = $("#maingridSpmm").ligerGrid({
                height: '95%',
                columns: [
                    {
                        display: '日期',
                        name: 'rq', align: 'left', width: 90, minWidth: 60, type: 'date', editor: {type: 'date'}
                    },
                    {
                        display: '股票代码',
                        name: 'code', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '股票名称',
                        name: 'name', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '操作',
                        name: 'caozuo', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '买卖价格',
                        name: 'jiage', minWidth: 90, editor: {type: 'float'},
                        render:function (item) {
                            // console.log(item.jiage);
                            return Math.abs(parseFloat(item.jiage));
                        }
                    },
                    {
                        display: '买卖数量',
                        name: 'sl', minWidth: 90, editor: {type: 'float'}
                    },
                    {
                        display: '买卖逻辑',
                        name: 'luoji', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '',
                        name: 'recid', width: 10, hide: true
                    }
                ], data: resultData, pageSize: 25, rownumbers: true, enabledEdit: false, onReload: spmmFresh,
                toolbar: {
                    items: [
                        {text: '增加', click: addSpmmRow, icon: 'add'},
                        {line: true},
                        {text: '修改', click: editSpmmRow, icon: 'edit'},
                        {line: true},
                        {text: '删除', click: deleteSpmmRow, icon: 'delete'}
                    ]
                }
            });
        }
    });
}

function spmmFresh() {
    // console.log("刷新");
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/spmm/index',
        success: function (data) {
            var resultData = JSON.parse(data);
            $grid.loadData(resultData);
            console.log("刷新完成");
        }
    });
}
function deleteSpmmRow() {
    var manager = $("#maingridSpmm").ligerGetGridManager();
    var row = manager.getSelectedRow();
    var recId = row['recid'];
    // alert(recId);
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/spmm/delete',
        data: {'recId': recId},
        success: function (data) {
            alert(data);
        }
    });
    manager.deleteSelectedRow();
}
function addSpmmRow() {
    var rq = new Date().format("yyyy-MM-dd");
    // console.log(win1);
    if (win1) {
        win1.show();
        showForm(rq);
    }
    // else {
    //     win1 = $.ligerDialog.open({ url: showForm(rq), width: 600,height:400,modal:false });
    // }
}
function showForm(val) {
    var rootDiv = $(".l-dialog-content.l-dialog-content-noimage");
    if($('#spmm_add').length<=1) {
        var lhbDiv = $('<div>', {'id': 'spmm_add', 'style': 'width:100%'});
        rootDiv.append(lhbDiv);
        var spmmTable = $('<table>', {'class': 'l-table-edit'});
        lhbDiv.append(spmmTable);
        var thTR = $('<tr>');
        spmmTable.append(thTR);
        var th1 = $('<td>', {'class': 'l-table-edit-td', 'align': 'right'});
        var th2 = $('<td>', {'class': 'l-table-edit-td', 'align': 'left'});
        var rqText = $('<input>', {'type': 'text', 'name': 'txt2', 'id': 'txt2', 'ltype': 'date'});
        th2.append(rqText);
        rqText.ligerDateEditor({initValue: val});
        th1.text("日期：");
        thTR.append(th1);
        thTR.append(th2);

        var thTR2 = $('<tr>');
        spmmTable.append(thTR2);
        var td1 = $('<td>', {'class': 'l-table-edit-td', 'align': 'right'});
        var td2 = $('<td>', {'class': 'l-table-edit-td', 'align': 'left'});
        var td3 = $('<td>', {'class': 'l-table-edit-td', 'align': 'right'});
        var td4 = $('<td>', {'class': 'l-table-edit-td', 'align': 'left'});
        var tdTxt3 = $('<input>', {'type': 'text', 'name': 'txt3', 'id': 'txt3'});
        var tdTxt4 = $('<input>', {'type': 'text', 'name': 'txt4', 'id': 'txt4','readOnly':'readOnly'});
        td2.append(tdTxt3);
        td4.append(tdTxt4);
        td1.text("股票代码：");
        td3.text("股票名称：");
        tdTxt3.ligerPopupEdit({
            condition: {
                prefixID: 'condtion_',
                fields: [{name: 'stockcode', type: 'text', label: '股票代码'}]
            },
            grid: getGridOptions(true),
            valueField: 'stockcode',
            textField: 'stockcode',
            width: 135,
            onSelected: function (e) {
                // console.log(e);
                tdTxt4.val(e.data[0].stockname);
            }
        });
        thTR2.append(td1);
        thTR2.append(td2);
        thTR2.append(td3);
        thTR2.append(td4);

        var thTR3 = $('<tr>');
        spmmTable.append(thTR3);
        var td5 = $('<td>', {'class': 'l-table-edit-td', 'align': 'right'});
        var td6 = $('<td>', {'class': 'l-table-edit-td', 'align': 'left'});
        var td7 = $('<td>', {'class': 'l-table-edit-td', 'align': 'right'});
        var td8 = $('<td>', {'class': 'l-table-edit-td', 'align': 'left'});
        var mmfx0 = $('<input>', {
            'type': 'radio',
            'name': 'mmfx',
            'value': '0',
            'style': 'width:50px'
        });
        var mmfx1 = $('<input>', {'type': 'radio', 'name': 'mmfx', 'value': '1', 'style': 'width:50px',
            'checked': 'true'});
        var tdTxt6 = $('<input>', {'type': 'text', 'name': 'txt6', 'id': 'txt6'});
        var label1 = $('<label>');
        var label2 = $('<label>');
        label1.text("卖");
        label2.text("买");
        td6.append(mmfx1);
        td6.append(label2);
        td6.append(mmfx0);
        td6.append(label1);
        td8.append(tdTxt6);
        td5.text("买卖方向：");
        td7.text("买卖逻辑：");
        thTR3.append(td5);
        thTR3.append(td6);
        thTR3.append(td7);
        thTR3.append(td8);
        $("#txt6").ligerComboBox(
            {
                url: "/spmm/luoji",
                valueField: 'luoji',
                textField: 'luoji',
                selectBoxWidth: 135,
                autocomplete: true,
                width: 135, keySupport: true,
                renderItem: function (e) {
                    var data = e.data, key = e.key;
                    var out = [];
                    out.push('<div>' + this._highLight(data.luoji, key) + '</div>');
                    out.push('<div class="desc">累计:' + data.counts + '</div>');
                    return out.join('');
                }
            }
        );


        var thTR4 = $('<tr>');
        spmmTable.append(thTR4);
        var td9 = $('<td>', {'class': 'l-table-edit-td', 'align': 'right'});
        var td10 = $('<td>', {'class': 'l-table-edit-td', 'align': 'left'});
        var td11 = $('<td>', {'class': 'l-table-edit-td', 'align': 'right'});
        var td12 = $('<td>', {'class': 'l-table-edit-td', 'align': 'left'});
        var tdTxt7 = $('<input>', {'type': 'text', 'name': 'txt7', 'id': 'txt7',});
        var tdTxt8 = $('<input>', {'type': 'text', 'name': 'txt8', 'id': 'txt8',});
        td10.append(tdTxt7);
        td12.append(tdTxt8);
        td9.text("价格：");
        td11.text("数量：");
        thTR4.append(td9);
        thTR4.append(td10);
        thTR4.append(td11);
        thTR4.append(td12);

        var btnDiv = $('<div>', {'style': 'width:100%;text-align:center'});
        lhbDiv.append(btnDiv);
        var btn1 = $('<input>', {
            'type': 'button',
            'click': saveSpmm,
            'id': 'btn1',
            'value': '保存',
            'class': 'l-button l-button-submit'
        });
        var btn2 = $('<input>', {
            'type': 'button',
            'click': closeSpmm,
            'id': 'btn2',
            'value': '关闭',
            'class': 'l-button l-button-reset'
        });
        btnDiv.append(btn1);
        btnDiv.append(btn2);
    }
}
function getGridOptions(checkbox) {
    var options = {
        columns: [
            { display: '股票代码', name: 'stockcode', align: 'right', width: 150, minWidth: 120 },
            { display: '股票名称', name: 'stockname',align: 'center', minWidth: 120, width: 150 },
            { display: '行业', name: 'hangye',align: 'center', minWidth: 120, width: 150 },
            { display: '标签', name: 'tag',align: 'center', minWidth: 120, width: 150 }
        ],
        switchPageSizeApplyComboBox: false,
        data:stockData,
        pageSize: 20,
        checkbox: checkbox
    };
    return options;
}

function saveSpmm() {
    var manager = $("#maingridSpmm").ligerGetGridManager();
    var row = manager.getSelectedRow();
    var recId ;
    try{
        recId = row['recid'];
    }catch (e){
        console.log(e);
    }
    var radio = $("[name=mmfx]");
    var caozuo=1;
    for(var i=0;i<radio.length;i++){
        if(radio[i].checked){
            caozuo = radio[i].value;
        }
    }
    var spmm={
        'recid':recId,
        'rq':$("#txt2").val(),
        'code':$("#txt3").val(),
        'name':$("#txt4").val(),
        'caozuo':caozuo,
        'jiage':$("#txt7").val(),
        'sl':$("#txt8").val(),
        'luoji':$("#txt6").val()
    };
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: '/spmm/save',
        data: JSON.stringify(spmm),
        success: function (data) {
            alert(data);
        }
    });
}
function closeSpmm() {
    $("#txt3").val("");
    $("#txt4").val("");
    $("#txt6").val("");
    $("#txt7").val("");
    $("#txt8").val("");
    win1.hide();
}
function getSelected() {
    var manager = $("#maingridSpmm").ligerGetGridManager();
    var row = manager.getSelectedRow();
    if (!row) {
        alert('请选择行');
        return;
    }
    alert(JSON.stringify(row));
}

function getData() {
    var manager = $("#maingridSpmm").ligerGetGridManager();
    var data = manager.getData();
    alert(JSON.stringify(data));
}

function editSpmmRow() {
    if (win1) {
        win1.show();
        showForm();
    }
    if($('#spmm_add').length<=1) {
        var manager = $("#maingridSpmm").ligerGetGridManager();
        var row = manager.getSelectedRow();
        // alert(JSON.stringify(row));
        try {
            row["rq"] = row["rq"].format("yyyy-MM-dd");
        } catch (e) {
            console.log(e);
            row["rq"] = row["rq"];
        }
        $("#txt2").val(row["rq"]);
        $("#txt3").val(row["code"]);
        $("#txt4").val(row["name"]);
        $("#txt6").val(row["luoji"]);
        $("#txt7").val(row["jiage"]);
        $("#txt8").val(row["sl"]);
        var radio = $("[name=mmfx]");
        var caozuo=row["caozuo"];
        for(var i=0;i<radio.length;i++){
            if(radio[i].value == caozuo ){
                radio[i].checked = true;
            }
        }
    }
}
/*
// e.value  e.text  e.data
function f_onSelected(e) {
    if (!e.data || !e.data.length) return;

    var grid = liger.get("maingridSpmm");

    var selected = e.data[0];
    grid.updateRow(grid.lastEditRow, {
        ztzdgn: selected.ztzdgn
    });

    var out = JSON.stringify(selected);
    $("#message").html('最后选择:' + out);
}
*/

