var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#queryWithResult").show();
    } else {
        $("#queryWithResult").hide();
    }
    $("#calculationResult").html("");
}

function connect() {
    var socket = new SockJS('/calculator-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        setConnected(true);
        stompClient.subscribe('/topic/calculationResult', function(calculationResult) {
            var calculationResultBody = JSON.parse(calculationResult.body);
            showCalculationResult(calculationResultBody.query, calculationResultBody.result);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
}

function sendQuery() {
    stompClient.send("/app/calculate", {}, JSON.stringify({
        'query': $("#query").val()
    }));
}

function showCalculationResult(query, result) {
    if (document.getElementById("calculationResult").rows.length == 10) {
        $("#calculationResult tr:last-child").remove();
    }
    $("#calculationResult").prepend(
        "<tr>" +
        "<td>" + query + "</td>" +
        "<td>" + "= " + "</td>" +
        "<td>" + result + "</td>" +
        "</tr>",
    );
}

$(function() {
    $("form").on('submit', function(e) {
        e.preventDefault();
    });
    $("#connect").click(function() {
        connect();
    });
    $("#disconnect").click(function() {
        disconnect();
    });
    $("#send").click(function() {
        sendQuery();
    });
});