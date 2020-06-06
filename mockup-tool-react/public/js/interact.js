// target elements with the "draggable" class
interact('.draggable')
  .draggable({
    // enable inertial throwing
    inertia: true,
    // keep the element within the area of it's parent
    modifiers: [
      interact.modifiers.restrict({
        restriction: "parent",
        endOnly: true,
        elementRect: { top: 0, left: 0, bottom: 1, right: 1 }
      }),
    ],
    // enable autoScroll
    autoScroll: true,

    // call this function on every dragmove event
    onmove: dragMoveListener,
    // call this function on every dragend event
    onend: function (event) {
      var textEl = event.target.querySelector('p');

      textEl && (textEl.textContent =
        'moved a distance of '
        + (Math.sqrt(Math.pow(event.pageX - event.x0, 2) +
          Math.pow(event.pageY - event.y0, 2) | 0))
          .toFixed(2) + 'px');
    }
  });

function dragMoveListener(event) {
  var target = event.target,
    // keep the dragged position in the data-x/data-y attributes
    x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
    y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

    for(var i=0;i<xOse.length;i++){
      if(Math.abs(x-xOse)<=1 && xMis-trenutniMisX<=15 && xMis-trenutniMisX>=0){
        x=xOse[i];
        //console.log("Prvi uslov");
        break;
      }
      else if(Math.abs(x+event.target.offsetWidth-xOse)<=1 && xMis-trenutniMisX<=15 && xMis-trenutniMisX>=0){
        //console.log("Drugi uslov");
        x=xOse[i]-event.target.offsetWidth;
        break;
      }
      else if(x<=xOse[i]+5 && x>=xOse[i]-5 && x!=xOse[i] && x+event.target.offsetWidth!=xOse[i]){
        //console.log("Treci uslov x:"+x+" xOse:"+xOse[i]);
        x=xOse[i];
        break;
      }
      else if(x+event.target.offsetWidth<=xOse[i]+5 && x+event.target.offsetWidth>=xOse[i]-5 && x!=xOse[i] && x+event.target.offsetWidth!=xOse[i]){
        //console.log("Cetvrti uslov"+(x+event.target.offsetWidth)+" xOse:"+xOse[i]);
        x=xOse[i]-event.target.offsetWidth;
        break;
      }
    }

    for(var i=0;i<yOse.length;i++){
      if(Math.abs(y-yOse)<=1 && yMis-trenutniMisY<=15 && yMis-trenutniMisY>=0){
        y=yOse[i];
        break;
      }
      else if(Math.abs(y+event.target.offsetHeight-yOse)<=1 && yMis-trenutniMisY<=15 && yMis-trenutniMisY>=0){
        y=yOse[i]-event.target.offsetHeight;
        break;
      }
      else if(y<=yOse[i]+5 && y>=yOse[i]-5 && y!=yOse[i] && y+event.target.offsetHeight!=yOse[i]){
        y=yOse[i];
        break;
      }
      else if(y+event.target.offsetHeight<=yOse[i]+5 && y+event.target.offsetHeight>=yOse[i]-5 && y!=yOse[i] && y+event.target.offsetHeight!=yOse[i]){
        y=yOse[i]-event.target.offsetHeight;
        break;
      }
    }
  // translate the element
  target.style.webkitTransform =
    target.style.transform =
    'translate(' + x + 'px, ' + y + 'px)';

  // update the posiion attributes
  target.setAttribute('data-x', x);
  target.setAttribute('data-y', y);

  /*var elems = document.getElementsByTagName('div');
  var highest = 0;
  for (var i = 0; i < elems.length; i++) {
    var zindex = document.defaultView.getComputedStyle(elems[i], null).getPropertyValue("z-index");
    if ((zindex > highest) && (zindex != 'auto')) {
      highest = zindex;
    }
  }
  console.log(highest);
  event.target.style.zIndex = parseInt(highest) + 1;*/
  //event.target.style.position = 'relative';
}

// this is used later in the resizing and gesture demos
window.dragMoveListener = dragMoveListener;


/* The dragging code for '.draggable' from the demo above
* applies to this demo as well so it doesn't have to be repeated. */


// enable draggables to be dropped into this
interact('.dropzone').dropzone({
  // only accept elements matching this CSS selector
  accept: '#yes-drop',
  // Require a 75% element overlap for a drop to be possible
  overlap: 0.75,

  // listen for drop related events:

  ondropactivate: function (event) {
    // add active dropzone feedback
    event.target.classList.add('drop-active')

  },
  ondragenter: function (event) {
    var draggableElement = event.relatedTarget;
    var dropzoneElement = event.target;

    // feedback the possibility of a drop
    dropzoneElement.classList.add('drop-target')
    draggableElement.classList.add('can-drop')
    draggableElement.textContent = 'Dragged in'

  },
  ondragleave: function (event) {
    // remove the drop feedback style
    event.target.classList.remove('drop-target')
    event.relatedTarget.classList.remove('can-drop')
    event.relatedTarget.textContent = 'Dragged out'

  },
  ondrop: function (event) {
    event.relatedTarget.textContent = 'Dropped'

  },
  ondropdeactivate: function (event) {
    // remove active dropzone feedback
    event.target.classList.remove('drop-active')
    event.target.classList.remove('drop-target')

  }
});

interact('.drag-drop')
  .draggable({
    inertia: true,
    modifiers: [
      interact.modifiers.restrict({
        restriction: "parent",
        endOnly: true,
        elementRect: { top: 0, left: 0, bottom: 1, right: 1 }
      })
    ],
    autoScroll: true,
    // dragMoveListener from the dragging demo above
    onmove: dragMoveListener
  });

interact('.resize-drag')
  .draggable({
    onstart: function(event) {
      ocitajSveOse(event.target);
    },
    onmove: window.dragMoveListener,
    modifiers: [
      interact.modifiers.restrict({
        restriction: 'parent',
        elementRect: { top: 0, left: 0, bottom: 1, right: 1 }
      })
    ]
  })
  .resizable({
    // resize from all edges and corners
    edges: { left: true, right: true, bottom: true, top: true },

    modifiers: [
      // keep the edges inside the parent
      interact.modifiers.restrictEdges({
        outer: 'parent',
        endOnly: true,
      }),

      // minimum size
      interact.modifiers.restrictSize({
        min: { width: 100, height: 50 },
      }),
    ],

    inertia: true
  })
  .on('resizemove', function (event) {
    var target = event.target,
      x = (parseFloat(target.getAttribute('data-x')) || 0),
      y = (parseFloat(target.getAttribute('data-y')) || 0);

    // update the element's style
    target.style.width = event.rect.width + 'px';
    target.style.height = event.rect.height + 'px';

    // translate when resizing from top or left edges
    x += event.deltaRect.left;
    y += event.deltaRect.top;

    target.style.webkitTransform = target.style.transform =
      'translate(' + x + 'px,' + y + 'px)';

    target.setAttribute('data-x', x);
    target.setAttribute('data-y', y);
    //target.textContent = Math.round(event.rect.width) + '\u00D7' + Math.round(event.rect.height);
  });