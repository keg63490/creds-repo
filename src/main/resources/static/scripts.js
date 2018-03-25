function findPassword(buttonPressed) {
    var token = document.getElementById("csrf-token").getAttribute("content");
    var idNum = buttonPressed.parentNode.children[0].value;

    $.ajax ({
        type : "POST",
        beforeSend: function(request) {
            request.setRequestHeader("x-csrf-token", token);
        },
        contentType : "application/json;",
        url : "/decrypt",
        data : JSON.stringify({"id" : idNum}),
        dataType : "json",
        success : function (data) {

            clearPasswords();
            document.getElementById("pw" + idNum).innerHTML = data['pw'];
        }
    });
}

function clearPasswords() {
    var parent = document.getElementById("tableRowsParent");
    var rowCount = parent.children.length;
    //console.log(rowCount);

    for (var i = 0; i < rowCount; i++){
        parent.children[i].children[3].innerHTML = "**********";
    }
}