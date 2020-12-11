/**
 * OWN JS SCRIPTS FOR HANGMANGAME
 * @author Mateusz Płonka
 */

/**
 * Allows user to isert only numeric values
 * @param {event} evt input event of input element to edit
 */
function validateNumber(evt) {
    var theEvent = evt || window.event;
    // Handle paste
    if (theEvent.type === 'paste') {
        key = event.clipboardData.getData('text/plain');
    } else {
        // Handle key press
        var key = theEvent.keyCode || theEvent.which;
        key = String.fromCharCode(key);
    }
    var regex = /[0-9]|\./;
    if (!regex.test(key)) {
        theEvent.returnValue = false;
        if (theEvent.preventDefault)
            theEvent.preventDefault();
    }
}


/**
 * Corrects display of word that is going to land in word bank
 * @param {String} input inserted word
 */
function addWordResult(input) {
    var insertWord = input.value;
    var msgBox = document.getElementById("addWordMsg");

    insertWord = insertWord.toUpperCase();
    insertWord = insertWord.replaceAll("[^A-Z0-9' ']+", "");
    insertWord = insertWord.replace(/[&\/\\#,+()$~%.'":*?<>{}]/g, '');
    insertWord.trim();

    msgBox.innerHTML = "Result: " + insertWord;
    input.value = insertWord;
}


/**
 * Corrects input value
 * @param {hmtl DOM input} element
 */
function tryLetter(element) {
    var input = element.value.toString();
    var input2 = "";
    input2 = input.replace(/[&\/\\#,+()$~%.'":*?<>{}]/g, '');
    input2 = input2.toUpperCase();
    input2.trim();

    if (input.toUpperCase() !== input2) {
        document.getElementById("msg").innerHTML = "Special character " + input + " is not allowed!";
    } else {
        document.getElementById("msg").innerHTML = "";
    }

    element.value = input2;
}

/**
 * Converts given word bank info popup
 * @param {int} exit [0] open pop-up, [1] close pop-up
 * @param {String} wordBank bank of words to display
 */
function infoCard(exit, ...wordBank)
{
    let body = document.body;
    let ICC = document.getElementById("ICC");
    let text = "";

    if (exit === 0)
    {
        var index = 1;
        body.style.overflow = "hidden";
        ICC.className = "ICC_active";
        text = "<div id='infoCard_0' class='infoCard animate__animated animate__fadeInDown'>\n";
        text += "<table>\n"
        text += "<tr>\n<th>Id</th>\n<th>Word</th>\n</tr>\n"
        for (let arg of wordBank) {
            text += "<tr><td>" + (index++) + "</td><td>" + arg + "</td></tr>"; }
        text += "</table><input type='button' value='Back' onclick='infoCard(1)'></div>";
        ICC.innerHTML = text;
    } else if (exit === 1)
    {
        body.style.overflow = "visible";
        ICC.className = "";
        ICC.innerHTML = "";
    }
}
