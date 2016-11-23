var aimEle = document.getElementById("myCanvas");
var modal = document.getElementById("modal");
var wrap = document.getElementById("wrap");

var ua = window.navigator.userAgent;
var isIOS = (/iPhone|iPad|iPod/i).test(ua),
    isAndroid = (/Android/i).test(ua);


aimEle.addEventListener(function(event) {
    event.preventDefault();
}, false);
//禁止回弹效果


document.addEventListener("touchstart", function(event) {
    event.preventDefault();
}, false);

document.addEventListener("touchmove", function(event) {
    event.preventDefault();
}, false);

document.getElementById("home").addEventListener("touchend", function(event) {
    event.preventDefault();
    modal.style.display = "none";
    wrap.classList.remove("bounce-in");

    document.querySelector("#gameContainer canvas").remove();
}, false);

var card = new lib.scrape({
    el: "#myCanvas",
    coverColor: "#e2e2e2",
    lineWidth: 30,
    activePercent: 0.6,
    font: {
        size: 20 * dpr + "px",
        color: '#9d9d9d',
        family: "微软雅黑",
        text: '抽奖刮奖区',
        x: 'center', // 取值可以是center或者数字
        y: 50 * dpr // 取值可以是center或者数字
    },
    onfinish: function() {
        //已经玩过
        hasPlayed = true;

        if(isPrince) {
            modal.style.display = "flex";
            wrap.classList.add("bounce-in");
            initAnimate();
        }else {
            var failEle = document.createElement("div");
            failEle.className = "fail fly";
            failEle.innerHTML = '<img src="http://gw.alicdn.com/mt/TB1niChLpXXXXX2XXXXXXXXXXXX-300-74.png">';
            document.body.appendChild(failEle);
        }
        countEle.textContent = 0;
    }
});

function createAwardElements(msg) {
    var p = document.createElement('p');
    p.innerHTML = msg;
    p.className = 'active-el';
    return p;
}


function doGame(hasPlayed, msg){
//重新调用这两个可以继续抽奖
    card.replace(createAwardElements(msg));
    if (!hasPlayed) {
        card.refresh();
    }
}




var dpr = window.devicePixelRatio > 1 ? 2 : 1;
var gameContainer = document.getElementById("gameContainer"),
    stageHeight = gameContainer.offsetHeight,
    stageWidth = gameContainer.offsetWidth;
function initAnimate(){
    var stage = new Hilo.Stage({
        renderType: "canvas",
        container: gameContainer,
        width: stageWidth,
        height: stageHeight
    });

    //start stage ticker
    var ticker = new Hilo.Ticker(60);
    ticker.addTick(stage);
    ticker.addTick(Hilo.Tween);
    ticker.start();

    var img = document.getElementById("texture");
    var particleSystem = new Hilo.ParticleSystem({
        x:0,
        y:0,
        emitNum:20,
        emitTime:1,
        particle:{
            frame:[
                [75, 236, 7, 11],
                [119, 223, 7, 17],
                [90, 223, 22, 17],
                [51, 202, 17, 46],
                [94, 59, 34, 59],
                [60, 160, 34, 42],
                [30, 99, 30, 99],
                [7, 240, 7, 11],
                [119, 206, 7, 17],
                [90, 206, 22, 17],
                [111, 160, 17, 46],
                [60, 59, 34, 59],
                [94, 118, 34, 42],
                [30, 0, 30, 99],
                [68, 236, 7, 11],
                [112, 223, 7, 17],
                [68, 219, 22, 17],
                [94, 160, 17, 46],
                [94, 0, 34, 59],
                [60, 118, 34, 42],
                [0, 99, 30, 99],
                [0, 240, 7, 11],
                [112, 206, 7, 17],
                [68, 202, 22, 17],
                [34, 198, 17, 46],
                [60, 0, 34, 59],
                [0, 198, 34, 42],
                [0, 0, 30, 99]
            ],
            image:img,
            life:22,
            alphaV:-.01,
            vxVar:300,
            vyVar:300,
            axVar:200,
            ayVar:200,
            scale: 1,
            rotationVar:360,
            rotationVVar:4,
            pivotX:.5,
            pivotY:.5
        }
    });
    stage.addChild(particleSystem);
    particleSystem.start();

    ticker.addTick({
        tick:function(){
            particleSystem.emitterX = Math.random() * stageWidth;
            particleSystem.emitterY = Math.random() * stageHeight;
        }
    })
}
