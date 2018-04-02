function spfx(tabid) {
    var rootDiv = $('<div>', {'id': 'spmmFx' + tabid, 'class': 'l-tab-content-item'});
    var lhbDiv = $('<div>', {'class': 'l-tab-content-item'});
    var clearDiv = $('<div>', {'class': 'l-clear'});
    var maingridDiv = $('<div>', {'id': 'maingridGfjgdSpmmFx'});
    lhbDiv.prepend(clearDiv);
    lhbDiv.append(maingridDiv);
    rootDiv.append(lhbDiv);
    $("div[tabid='"+tabid+"']").append(rootDiv);
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/spmm/fx',
        success: function (data) {
            var resultData3 = JSON.parse(data);
            // console.log(resultData2);
            $("#maingridGfjgdSpmmFx").ligerGrid({
                height: '95%',
                columns: [
                    {
                        display: '证券代码',
                        name: 'code', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '证券名称',
                        name: 'name', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '证券数量',
                        name: 'sl', minWidth: 90, editor: {type: 'text'}
                    },
                    {
                        display: '盈亏金额',
                        name: 'ykje', minWidth: 90, editor: {type: 'text'},
                        render:function (item) {
                            if (parseFloat(item.ykje)>0){
                                return '<span style="color:red">'+parseFloat(item.ykje).toFixed(2) + '</span>';
                            }else{
                                return '<span style="color:green">'+parseFloat(item.ykje).toFixed(2) + '</span>';
                            }
                        }
                    },
                    {
                        display: '持仓天数',
                        name: 'ccts', minWidth: 90, editor: {type: 'text'}
                    }
                ], data: resultData3, pageSize: 25, rownumbers: true
            });
        }
    });
}