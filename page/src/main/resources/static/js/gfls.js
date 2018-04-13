function gfls(tabid) {
    var rootDiv = $('<div>', {'id': 'gfjgd' + tabid, 'class': 'l-tab-content-item'});
    var lhbDiv = $('<div>', {'class': 'l-tab-content-item'});
    var clearDiv = $('<div>', {'class': 'l-clear'});
    var maingridDiv = $('<div>', {'id': 'maingridGfjgd'});
    lhbDiv.prepend(clearDiv);
    lhbDiv.append(maingridDiv);
    rootDiv.append(lhbDiv);
    $("div[tabid='"+tabid+"']").append(rootDiv);
    var date1 = new Date();
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/gfjgd/index',
        success: function (data) {
            var resultData2 = JSON.parse(data);
            // console.log(resultData2);
            $("#maingridGfjgd").ligerGrid({
                height: '95%',
                columns: [
                    {
                        display: '成交日期',
                        name: 'stockdate', align: 'left', width: 90, minWidth: 60, type: 'date', editor: {type: 'date'}
                    },
                    {
                        display: '证券代码',
                        name: 'stockcode', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '证券名称',
                        name: 'stockname', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '操作',
                        name: 'oper', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '成交数量',
                        name: 'stocknum', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '成交均价',
                        name: 'stockprice', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '成交金额',
                        name: 'stockallprice', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '余额',
                        name: 'stocksl', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '可用余额',
                        name: 'stockkysl', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '发生金额',
                        name: 'fsje', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '印花税',
                        name: 'yhs', editor: {type: 'text'}
                    },
                    {
                        display: '合同编号',
                        name: 'htbh', editor: {type: 'text'}
                    },
                    {
                        display: '可用金额',
                        name: 'kyje', editor: {type: 'text'}
                    },
                    {
                        display: '过户费',
                        name: 'ghf', editor: {type: 'text'}
                    },
                    {
                        display: '经手费',
                        name: 'jsf', editor: {type: 'text'}
                    },
                    {
                        display: '证管费',
                        name: 'zgf', editor: {type: 'text'}
                    },
                    {
                        display: '佣金',
                        name: 'yj', editor: {type: 'text'}
                    },
                    {
                        display: '',
                        name: 'recid', width: 10, hide: true
                    }
                ], data: resultData2, pageSize: 25, rownumbers: true
            });
        }
    });
    // console.log("查询广发交割单耗时",new Date().getTime()-date1.getTime());
}