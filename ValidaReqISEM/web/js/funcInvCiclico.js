/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$('#buscarDescrip').keyup(function() {
    var descripcion = $('#buscarDescrip').val();
    $('#buscarDescrip').autocomplete({
        source: "JQInvenCiclico?accion=buscaDescrip&descrip=" + descripcion,
        minLenght: 2,
        select: function(event, ui) {
            $('#buscarDescrip').val(ui.item.DesPro);
            return false;
        }
    }).data('ui-autocomplete')._renderItem = function(ul, item) {
        return $('<li>')
                .data('ui-autocomplete-item', item)
                .append('<a>' + item.DesPro + '</a>')
                .appendTo(ul);
    };
    //alert(descripcion);
});

$('#btnCapturar').click(function() {
    var cantSol = parseInt($('#CantSol').val());
    var cantMax = parseInt($('#CantMax').val());
    var obser = $('#Observaciones').val();
    if (cantSol > cantMax) {
        if (obser === "") {
            alert('Solicita mas de lo establecido, ingrese las razones de solicitud');
        } else {
            return true;
        }
    } else{
        return true;
    }
    return false;
});
