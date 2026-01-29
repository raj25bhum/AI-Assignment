let states = [
    [[1,2,3],[4,0,6],[7,5,8]],
    [[1,2,3],[4,5,6],[7,0,8]],
    [[1,2,3],[4,5,6],[7,8,0]]
];

let step=0;

function draw(){
    const board=document.getElementById("board");
    board.innerHTML="";
    let s=states[step];

    for(let i=0;i<3;i++){
        for(let j=0;j<3;j++){
            let d=document.createElement("div");
            let val=s[i][j];
            d.className="tile";
            if(val==0){
                d.classList.add("blank");
                d.innerHTML="";
            } else d.innerHTML=val;
            board.appendChild(d);
        }
    }
}

function next(){
    if(step<states.length-1){
        step++;
        draw();
    }
}

function reset(){
    step=0;
    draw();
}

draw();
