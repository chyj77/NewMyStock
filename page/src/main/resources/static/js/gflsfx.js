function gflsfx(tabid) {
    var rootDiv = $('<div>', {'id': 'gfjgdfx' + tabid, 'class': 'l-tab-content-item'});
    var lhbDiv = $('<div>', {'class': 'l-tab-content-item'});
    var clearDiv = $('<div>', {'class': 'l-clear'});
    var maingridDiv = $('<div>', {'id': 'maingridGfjgdfx'});
    lhbDiv.prepend(clearDiv);
    lhbDiv.append(maingridDiv);
    rootDiv.append(lhbDiv);
    $("div[tabid='"+tabid+"']").append(rootDiv);
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/gfjgd/fx',
        success: function (data) {
            var resultData1 = JSON.parse(data);
            console.log(resultData1);
            $("#maingridGfjgdfx").ligerGrid({
                height: '95%',
                columns: [
                    {
                        display: '证券代码',
                        name: 'stockcode', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '证券名称',
                        name: 'stockname', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '证券数量',
                        name: 'stocknum', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '盈亏金额',
                        name: 'fsje', minWidth: 90, editor: {type: 'text'},
                        render:function (item) {
                            if (parseFloat(item.fsje)>0){
                                return '<span style="color:red">'+parseFloat(item.fsje).toFixed(2) + '</span>';
                            }else{
                                return '<span style="color:green">'+parseFloat(item.fsje).toFixed(2) + '</span>';
                            }
                        }
                    },
                    {
                        display: '涨跌幅',
                        name: 'zdf', minWidth: 90, editor: {type: 'text'},
                        render:function (item) {
                            if (parseFloat(item.zdf)>0){
                                return '<span style="color:red">'+parseFloat(item.zdf).toFixed(2) + '%</span>';
                            }else{
                                return '<span style="color:green">'+parseFloat(item.zdf).toFixed(2) + '%</span>';
                            }
                        }
                    }
                ], data: resultData1, pageSize: 25, rownumbers: true
            });
        }
    });
}