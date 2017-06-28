
var API_url = getBaseUrl() + "api";
var login_url = getBaseUrl() + "authen";
var QueryString = allParam();

$(document).ready(function () {
    initAutoComplete();
    addPicture(QueryString.word);
    initUpdateWord();
    initAuthen();
    initDelete();
    initModify();
    showHideLoginSection();
});


function getBaseUrl() {
    var re = new RegExp(/^.*\//);
    return re.exec(window.location.href);
}

function IsJsonString(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}

function addPicture(word) {
    if(!word) return;
    
    var baseURL = getBaseUrl();
    var API_url = baseURL + "api";

    $.get(API_url, {word: word, requestType: "image"}, function (data) {
        data = toArray(data);
        $("#imageArea").html("");

        var i = 1;

        for (i = 0; i < 6; i++) {
            item = data[i];
            if(!item)return;
            var img = $('<img>'); //Equivalent: $(document.createElement('img'))
            img.attr('src', item.toString());
            img.appendTo('#images');
            console.log(i);
        }
        ;
    });
}

function initAutoComplete() {
    var wordBox = $("#wordBox");
    var autocompleteArea = $("#autoCompleteBox");

    wordBox.keyup(function (e) {
        /*if (e.which == 13) {
         window.location.href = getBaseUrl() + "view?word=" + item.toString();
         }*/
        $.get(API_url, {word: wordBox.val(), requestType: "autocomplete"},
                function (data) {
                    console.log(data);

                    data = toArray(data);
                    autocompleteArea.html("");

                    data.forEach(function (item) {
                        if (item.toString().length < 2)
                            return true;
                        var tag = "<a class='autoCompleteWord' href='" + getBaseUrl() + "view?word=" + item.toString() + "'>" + item.toString().toUpperCase() + "</a>";

                        $("#autoCompleteBox").append(tag)
                    })
                })
    })
}

function allParam() {
    var QueryString = function () {
        // This function is anonymous, is executed immediately and 
        // the return value is assigned to QueryString!
        var query_string = {};
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            // If first entry with this name
            if (typeof query_string[pair[0]] === "undefined") {
                query_string[pair[0]] = decodeURIComponent(pair[1]);
                // If second entry with this name
            } else if (typeof query_string[pair[0]] === "string") {
                var arr = [query_string[pair[0]], decodeURIComponent(pair[1])];
                query_string[pair[0]] = arr;
                // If third or later entry with this name
            } else {
                query_string[pair[0]].push(decodeURIComponent(pair[1]));
            }
        }
        return query_string;
    }();

    return QueryString;

}

function toArray(s) {
    arrayString = s.replace(/[\[\]&]+/g, '');
    return arrayString.split(',');

}

function initUpdateWord() {
    $("#changeConfirm").click(function () {

        var word = $("#wordToChange").val();
        var translation = $("#wordToChangeContent").val();
        if (!word || !translation) {
            return;
        }

        $.get(API_url, {word: word, requestType: "update", translation: translation}, function (data) {
            window.location.href = getBaseUrl() + "view?word=" + word;
        });
    })

}

function initAuthen() {
    $("#loginBtn").click(function () {
        var username = $("#usernameBox").val();
        var passwords = $("#passwordsBox").val();


        $.get(login_url, {username: username, passwords: passwords}, function (data) {
            location.reload(true);
        });

    })

    $("#cancelLoginBtn").click(function () {
        $("#loginform").hide("fast");
    })

}

function initDelete() {
    $("#deleteWord").click(function () {
        var word = QueryString.word;
        $.get(API_url, {word: word, requestType: "delete"}, function (data) {
            window.location.href = getBaseUrl() + "view?word=" + word;
        });
    })
}

function initModify() {
    $("#editWord").click(function () {
        var word = QueryString.word;
        $("#wordToChange").val(word);
        $("#modField").show("fast");
    })

    $("#addWord").click(function () {
        $("#wordToChange").val("");
        $("#modField").show("fast");
    })

    $("#changeCancel").click(function () {
        $("#modField").hide("fast");
    })

}





function showHideLoginSection() {
    var button = $("#authen");
    if (button.text() != "LOG OUT") {
        button.click(function () {

            $("#loginform").show("fast");
        })
    } else {
        button.click(function () {
            $.get(login_url, {logout: "1"}, function (data) {
                location.reload(true);
            });
        });

    }
}