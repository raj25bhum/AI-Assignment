window.onload = function() {
	var num;
	var box;
	var ctx;
	var turn = 1;
	var filled;
	var symbol;
	var winner;
	var gameOver = false;
	var human = 'X';
	var ai = 'O';
	var result = {};
	filled = new Array();
	symbol = new Array();
	winner = [[0,1,2],[3,4,5],[6,7,8],[0,3,6],[1,4,7],[2,5,8],[0,4,8],[2,4,6]];
	for(var i=0; i<9; i++) {
		filled[i] = false;
		symbol[i] = '';
	}
	var n = document.getElementById("new");
	n.addEventListener("click",newGame);
	function newGame() {
		document.location.reload();
	}
	document.getElementById("tic").addEventListener("click",function(e){
		boxClick(e.target.id);
	});
	function drawX() {
		box.style.backgroundColor = "#fb5181";
		ctx.beginPath();
		ctx.moveTo(15,15);
		ctx.lineTo(85,85);
		ctx.moveTo(85,15);
		ctx.lineTo(15,85);
		ctx.lineWidth = 21;
		ctx.lineCap = "round";
		ctx.strokeStyle = "white";
		ctx.stroke();
		ctx.closePath();
		
		symbol[num-1] = human;
	}
	function drawO(next) {
		box.style.backgroundColor = "#93f273";
		ctx.beginPath();
		ctx.arc(50,50,35,0,2*Math.PI);
		ctx.lineWidth = 20;
		ctx.strokeStyle = "white";
		ctx.stroke();
		ctx.closePath();
		
		symbol[next] = ai;
	}
	function winnerCheck(symbol,player) {
		for(var j=0;j<winner.length;j++) {
			if((symbol[winner[j][0]] == player) && (symbol[winner[j][1]] == player) && (symbol[winner[j][2]] == player)) {
				return true;
			}
		}
		return false;
	}
	function boxClick(numId) {
		box = document.getElementById(numId);
		ctx = box.getContext("2d");
		switch(numId) {
			case "canvas1": num = 1;
							break;
			case "canvas2": num = 2;
							break;
			case "canvas3": num = 3;
							break;
			case "canvas4": num = 4;
							break;
			case "canvas5": num = 5;
							break;
			case "canvas6": num = 6;
							break;
			case "canvas7": num = 7;
							break;
			case "canvas8": num = 8;
							break;
			case "canvas9": num = 9;
							break;
		}
		
		if(filled[num-1] === false) {
			if(gameOver === false) {
				if(turn%2 !== 0) {
					drawX();
					turn++;
					filled[num-1] = true;
					
					if(winnerCheck(symbol,symbol[num-1]) === true) {
						document.getElementById("result").innerText = "Player '" + symbol[num-1] + "' won!";
						gameOver = true;
						
					}
					
					if(turn > 9 && gameOver !== true) {
						document.getElementById("result").innerText = "GAME OVER! IT WAS A DRAW!";
						return;
					}
					
					if(turn%2 == 0) {
						playAI();
					}
				}
			}
			else {
				alert("Game over. Please click the New Game button to start again");
			}
		}
		else {
			alert("This box was already filled. Please click on another one.")
		}
	}
	function emptyBoxes(newSymbol) {
		var j = 0;
		var empty = [];
		for(var i=0; i<newSymbol.length; i++) {
			if(newSymbol[i] !== 'X' && newSymbol[i] !== 'O') {
				empty[j] = i;
				j++;
			}
		}
		return empty;
	}
	function playAI() {
		var nextMove = miniMax(symbol,ai);
		var nextId = "canvas" + (nextMove.id + 1);
		box = document.getElementById(nextId);
		ctx = box.getContext("2d");
		if(gameOver === false) {
			if(turn%2 === 0) { 
				drawO(nextMove.id);
				turn++;
				filled[nextMove.id] = true;
				if(winnerCheck(symbol, symbol[nextMove.id]) === true) {
					document.getElementById("result").innerText = "Player '" + symbol[nextMove.id] + "' won!";
					gameOver = true;
				}
				if(turn > 9 && gameOver !== true) {
					document.getElementById("result").innerText = "GAME OVER! IT WAS A DRAW!";
				}
			}
		}
		
		else {
			alert("Game is over. Please click the New Game button to start again");
		}
	}
	function miniMax(newSymbol, player) {
		var empty = [];
		empty = emptyBoxes(newSymbol); 
		
		if(winnerCheck(newSymbol,human)) {
			return { score: -10 }; 
		}
		else if(winnerCheck(newSymbol,ai)) {
			return { score: 10 }; 
		}
		else if(empty.length === 0) {
			if(winnerCheck(newSymbol,human)) {
				return { score: -10 };
			}
			else if(winnerCheck(newSymbol,ai)) {
				return { score : 10 };
			}
			return { score: 0 };
		}
		var posMoves = []; 
		for(var i=0; i<empty.length; i++) {
			var curMove = {};
			curMove.id = empty[i]; 
			newSymbol[empty[i]] = player;
			
			if(player === ai) {
				result = miniMax(newSymbol, human);
				curMove.score = result.score;
			}
			else {
				result = miniMax(newSymbol, ai);
				curMove.score = result.score;
			}
			newSymbol[empty[i]] = '';
			posMoves.push(curMove); 
			
		} 
		var bestMove;
		if(player === ai) {
			var highestScore = -1000;
			for(var j=0; j<posMoves.length;j++) {
				if(posMoves[j].score > highestScore) {
					highestScore = posMoves[j].score;
					bestMove = j;
				}
			}
		}
		else {
			var lowestScore = 1000;
			for(var j=0; j<posMoves.length;j++) {
				if(posMoves[j].score < lowestScore) {
					lowestScore = posMoves[j].score;
					bestMove = j;
				}
			}
		}
		return posMoves[bestMove];
	}
};