import{B as $,s as B,o as u,c as f,m as l,av as K,P as y,Q as w,a,i as x,Z as k,ac as V,G as A,h as S,w as h,J as D,L as T,K as C,T as W,t as G,b as n,r as N,e as O,aT as j,H as J}from"./index-DZ6pyUV_.js";import{s as Z}from"./index-C8itqLoL.js";import{s as Q}from"./index-0as0sGfI.js";import{s as _}from"./index-BxjYiGRd.js";import{s as U}from"./index-BEj4KcLD.js";import{a as z}from"./index-D-qnh6E1.js";import{s as ee}from"./index-C-eCB2nK.js";var te=({dt:e})=>`
.p-skeleton {
    overflow: hidden;
    background: ${e("skeleton.background")};
    border-radius: ${e("skeleton.border.radius")};
}

.p-skeleton::after {
    content: "";
    animation: p-skeleton-animation 1.2s infinite;
    height: 100%;
    left: 0;
    position: absolute;
    right: 0;
    top: 0;
    transform: translateX(-100%);
    z-index: 1;
    background: linear-gradient(90deg, rgba(255, 255, 255, 0), ${e("skeleton.animation.background")}, rgba(255, 255, 255, 0));
}

[dir='rtl'] .p-skeleton::after {
    animation-name: p-skeleton-animation-rtl;
}

.p-skeleton-circle {
    border-radius: 50%;
}

.p-skeleton-animation-none::after {
    animation: none;
}

@keyframes p-skeleton-animation {
    from {
        transform: translateX(-100%);
    }
    to {
        transform: translateX(100%);
    }
}

@keyframes p-skeleton-animation-rtl {
    from {
        transform: translateX(100%);
    }
    to {
        transform: translateX(-100%);
    }
}
`,ne={root:{position:"relative"}},re={root:function(t){var i=t.props;return["p-skeleton p-component",{"p-skeleton-circle":i.shape==="circle","p-skeleton-animation-none":i.animation==="none"}]}},se=$.extend({name:"skeleton",style:te,classes:re,inlineStyles:ne}),ae={name:"BaseSkeleton",extends:B,props:{shape:{type:String,default:"rectangle"},size:{type:String,default:null},width:{type:String,default:"100%"},height:{type:String,default:"1rem"},borderRadius:{type:String,default:null},animation:{type:String,default:"wave"}},style:se,provide:function(){return{$pcSkeleton:this,$parentInstance:this}}},P={name:"Skeleton",extends:ae,inheritAttrs:!1,computed:{containerStyle:function(){return this.size?{width:this.size,height:this.size,borderRadius:this.borderRadius}:{width:this.width,height:this.height,borderRadius:this.borderRadius}}}};function ie(e,t,i,o,d,s){return u(),f("div",l({class:e.cx("root"),style:[e.sx("root"),s.containerStyle],"aria-hidden":"true"},e.ptmi("root")),null,16)}P.render=ie;var oe=({dt:e})=>`
.p-scrollpanel-content-container {
    overflow: hidden;
    width: 100%;
    height: 100%;
    position: relative;
    z-index: 1;
    float: left;
}

.p-scrollpanel-content {
    height: calc(100% + calc(2 * ${e("scrollpanel.bar.size")}));
    width: calc(100% + calc(2 * ${e("scrollpanel.bar.size")}));
    padding-inline: 0 calc(2 * ${e("scrollpanel.bar.size")});
    padding-block: 0 calc(2 * ${e("scrollpanel.bar.size")});
    position: relative;
    overflow: auto;
    box-sizing: border-box;
    scrollbar-width: none;
}

.p-scrollpanel-content::-webkit-scrollbar {
    display: none;
}

.p-scrollpanel-bar {
    position: relative;
    border-radius: ${e("scrollpanel.bar.border.radius")};
    z-index: 2;
    cursor: pointer;
    opacity: 0;
    outline-color: transparent;
    background: ${e("scrollpanel.bar.background")};
    border: 0 none;
    transition: outline-color ${e("scrollpanel.transition.duration")}, opacity ${e("scrollpanel.transition.duration")};
}

.p-scrollpanel-bar:focus-visible {
    box-shadow: ${e("scrollpanel.bar.focus.ring.shadow")};
    outline: ${e("scrollpanel.barfocus.ring.width")} ${e("scrollpanel.bar.focus.ring.style")} ${e("scrollpanel.bar.focus.ring.color")};
    outline-offset: ${e("scrollpanel.barfocus.ring.offset")};
}

.p-scrollpanel-bar-y {
    width: ${e("scrollpanel.bar.size")};
    inset-block-start: 0;
}

.p-scrollpanel-bar-x {
    height: ${e("scrollpanel.bar.size")};
    inset-block-end: 0;
}

.p-scrollpanel-hidden {
    visibility: hidden;
}

.p-scrollpanel:hover .p-scrollpanel-bar,
.p-scrollpanel:active .p-scrollpanel-bar {
    opacity: 1;
}

.p-scrollpanel-grabbed {
    user-select: none;
}
`,le={root:"p-scrollpanel p-component",contentContainer:"p-scrollpanel-content-container",content:"p-scrollpanel-content",barX:"p-scrollpanel-bar p-scrollpanel-bar-x",barY:"p-scrollpanel-bar p-scrollpanel-bar-y"},ce=$.extend({name:"scrollpanel",style:oe,classes:le}),ue={name:"BaseScrollPanel",extends:B,props:{step:{type:Number,default:5}},style:ce,provide:function(){return{$pcScrollPanel:this,$parentInstance:this}}},F={name:"ScrollPanel",extends:ue,inheritAttrs:!1,initialized:!1,documentResizeListener:null,documentMouseMoveListener:null,documentMouseUpListener:null,frame:null,scrollXRatio:null,scrollYRatio:null,isXBarClicked:!1,isYBarClicked:!1,lastPageX:null,lastPageY:null,timer:null,outsideClickListener:null,data:function(){return{orientation:"vertical",lastScrollTop:0,lastScrollLeft:0}},mounted:function(){this.$el.offsetParent&&this.initialize()},updated:function(){!this.initialized&&this.$el.offsetParent&&this.initialize()},beforeUnmount:function(){this.unbindDocumentResizeListener(),this.frame&&window.cancelAnimationFrame(this.frame)},methods:{initialize:function(){this.moveBar(),this.bindDocumentResizeListener(),this.calculateContainerHeight()},calculateContainerHeight:function(){var t=getComputedStyle(this.$el),i=getComputedStyle(this.$refs.xBar),o=K(this.$el)-parseInt(i.height,10);t["max-height"]!=="none"&&o===0&&(this.$refs.content.offsetHeight+parseInt(i.height,10)>parseInt(t["max-height"],10)?this.$el.style.height=t["max-height"]:this.$el.style.height=this.$refs.content.offsetHeight+parseFloat(t.paddingTop)+parseFloat(t.paddingBottom)+parseFloat(t.borderTopWidth)+parseFloat(t.borderBottomWidth)+"px")},moveBar:function(){var t=this;if(this.$refs.content){var i=this.$refs.content.scrollWidth,o=this.$refs.content.clientWidth,d=(this.$el.clientHeight-this.$refs.xBar.clientHeight)*-1;this.scrollXRatio=o/i;var s=this.$refs.content.scrollHeight,r=this.$refs.content.clientHeight,b=(this.$el.clientWidth-this.$refs.yBar.clientWidth)*-1;this.scrollYRatio=r/s,this.frame=this.requestAnimationFrame(function(){t.$refs.xBar&&(t.scrollXRatio>=1?(t.$refs.xBar.setAttribute("data-p-scrollpanel-hidden","true"),!t.isUnstyled&&y(t.$refs.xBar,"p-scrollpanel-hidden")):(t.$refs.xBar.setAttribute("data-p-scrollpanel-hidden","false"),!t.isUnstyled&&w(t.$refs.xBar,"p-scrollpanel-hidden"),t.$refs.xBar.style.cssText="width:"+Math.max(t.scrollXRatio*100,10)+"%; inset-inline-start:"+Math.abs(t.$refs.content.scrollLeft)/i*100+"%;bottom:"+d+"px;")),t.$refs.yBar&&(t.scrollYRatio>=1?(t.$refs.yBar.setAttribute("data-p-scrollpanel-hidden","true"),!t.isUnstyled&&y(t.$refs.yBar,"p-scrollpanel-hidden")):(t.$refs.yBar.setAttribute("data-p-scrollpanel-hidden","false"),!t.isUnstyled&&w(t.$refs.yBar,"p-scrollpanel-hidden"),t.$refs.yBar.style.cssText="height:"+Math.max(t.scrollYRatio*100,10)+"%; top: calc("+t.$refs.content.scrollTop/s*100+"% - "+t.$refs.xBar.clientHeight+"px); inset-inline-end:"+b+"px;"))})}},onYBarMouseDown:function(t){this.isYBarClicked=!0,this.$refs.yBar.focus(),this.lastPageY=t.pageY,this.$refs.yBar.setAttribute("data-p-scrollpanel-grabbed","true"),!this.isUnstyled&&y(this.$refs.yBar,"p-scrollpanel-grabbed"),document.body.setAttribute("data-p-scrollpanel-grabbed","true"),!this.isUnstyled&&y(document.body,"p-scrollpanel-grabbed"),this.bindDocumentMouseListeners(),t.preventDefault()},onXBarMouseDown:function(t){this.isXBarClicked=!0,this.$refs.xBar.focus(),this.lastPageX=t.pageX,this.$refs.yBar.setAttribute("data-p-scrollpanel-grabbed","false"),!this.isUnstyled&&y(this.$refs.xBar,"p-scrollpanel-grabbed"),document.body.setAttribute("data-p-scrollpanel-grabbed","false"),!this.isUnstyled&&y(document.body,"p-scrollpanel-grabbed"),this.bindDocumentMouseListeners(),t.preventDefault()},onScroll:function(t){this.lastScrollLeft!==t.target.scrollLeft?(this.lastScrollLeft=t.target.scrollLeft,this.orientation="horizontal"):this.lastScrollTop!==t.target.scrollTop&&(this.lastScrollTop=t.target.scrollTop,this.orientation="vertical"),this.moveBar()},onKeyDown:function(t){if(this.orientation==="vertical")switch(t.code){case"ArrowDown":{this.setTimer("scrollTop",this.step),t.preventDefault();break}case"ArrowUp":{this.setTimer("scrollTop",this.step*-1),t.preventDefault();break}case"ArrowLeft":case"ArrowRight":{t.preventDefault();break}}else if(this.orientation==="horizontal")switch(t.code){case"ArrowRight":{this.setTimer("scrollLeft",this.step),t.preventDefault();break}case"ArrowLeft":{this.setTimer("scrollLeft",this.step*-1),t.preventDefault();break}case"ArrowDown":case"ArrowUp":{t.preventDefault();break}}},onKeyUp:function(){this.clearTimer()},repeat:function(t,i){this.$refs.content[t]+=i,this.moveBar()},setTimer:function(t,i){var o=this;this.clearTimer(),this.timer=setTimeout(function(){o.repeat(t,i)},40)},clearTimer:function(){this.timer&&clearTimeout(this.timer)},onDocumentMouseMove:function(t){this.isXBarClicked?this.onMouseMoveForXBar(t):this.isYBarClicked?this.onMouseMoveForYBar(t):(this.onMouseMoveForXBar(t),this.onMouseMoveForYBar(t))},onMouseMoveForXBar:function(t){var i=this,o=t.pageX-this.lastPageX;this.lastPageX=t.pageX,this.frame=this.requestAnimationFrame(function(){i.$refs.content.scrollLeft+=o/i.scrollXRatio})},onMouseMoveForYBar:function(t){var i=this,o=t.pageY-this.lastPageY;this.lastPageY=t.pageY,this.frame=this.requestAnimationFrame(function(){i.$refs.content.scrollTop+=o/i.scrollYRatio})},onFocus:function(t){this.$refs.xBar.isSameNode(t.target)?this.orientation="horizontal":this.$refs.yBar.isSameNode(t.target)&&(this.orientation="vertical")},onBlur:function(){this.orientation==="horizontal"&&(this.orientation="vertical")},onDocumentMouseUp:function(){this.$refs.yBar.setAttribute("data-p-scrollpanel-grabbed","false"),!this.isUnstyled&&w(this.$refs.yBar,"p-scrollpanel-grabbed"),this.$refs.xBar.setAttribute("data-p-scrollpanel-grabbed","false"),!this.isUnstyled&&w(this.$refs.xBar,"p-scrollpanel-grabbed"),document.body.setAttribute("data-p-scrollpanel-grabbed","false"),!this.isUnstyled&&w(document.body,"p-scrollpanel-grabbed"),this.unbindDocumentMouseListeners(),this.isXBarClicked=!1,this.isYBarClicked=!1},requestAnimationFrame:function(t){var i=window.requestAnimationFrame||this.timeoutFrame;return i(t)},refresh:function(){this.moveBar()},scrollTop:function(t){var i=this.$refs.content.scrollHeight-this.$refs.content.clientHeight;t=t>i?i:t>0?t:0,this.$refs.content.scrollTop=t},timeoutFrame:function(t){setTimeout(t,0)},bindDocumentMouseListeners:function(){var t=this;this.documentMouseMoveListener||(this.documentMouseMoveListener=function(i){t.onDocumentMouseMove(i)},document.addEventListener("mousemove",this.documentMouseMoveListener)),this.documentMouseUpListener||(this.documentMouseUpListener=function(i){t.onDocumentMouseUp(i)},document.addEventListener("mouseup",this.documentMouseUpListener))},unbindDocumentMouseListeners:function(){this.documentMouseMoveListener&&(document.removeEventListener("mousemove",this.documentMouseMoveListener),this.documentMouseMoveListener=null),this.documentMouseUpListener&&(document.removeEventListener("mouseup",this.documentMouseUpListener),this.documentMouseUpListener=null)},bindDocumentResizeListener:function(){var t=this;this.documentResizeListener||(this.documentResizeListener=function(){t.moveBar()},window.addEventListener("resize",this.documentResizeListener))},unbindDocumentResizeListener:function(){this.documentResizeListener&&(window.removeEventListener("resize",this.documentResizeListener),this.documentResizeListener=null)}},computed:{contentId:function(){return this.$id+"_content"}}},de=["id"],pe=["aria-controls","aria-valuenow"],me=["aria-controls","aria-valuenow"];function fe(e,t,i,o,d,s){return u(),f("div",l({class:e.cx("root")},e.ptmi("root")),[a("div",l({class:e.cx("contentContainer")},e.ptm("contentContainer")),[a("div",l({ref:"content",id:s.contentId,class:e.cx("content"),onScroll:t[0]||(t[0]=function(){return s.onScroll&&s.onScroll.apply(s,arguments)}),onMouseenter:t[1]||(t[1]=function(){return s.moveBar&&s.moveBar.apply(s,arguments)})},e.ptm("content")),[x(e.$slots,"default")],16,de)],16),a("div",l({ref:"xBar",class:e.cx("barx"),tabindex:"0",role:"scrollbar","aria-orientation":"horizontal","aria-controls":s.contentId,"aria-valuenow":d.lastScrollLeft,onMousedown:t[2]||(t[2]=function(){return s.onXBarMouseDown&&s.onXBarMouseDown.apply(s,arguments)}),onKeydown:t[3]||(t[3]=function(r){return s.onKeyDown(r)}),onKeyup:t[4]||(t[4]=function(){return s.onKeyUp&&s.onKeyUp.apply(s,arguments)}),onFocus:t[5]||(t[5]=function(){return s.onFocus&&s.onFocus.apply(s,arguments)}),onBlur:t[6]||(t[6]=function(){return s.onBlur&&s.onBlur.apply(s,arguments)})},e.ptm("barx"),{"data-pc-group-section":"bar"}),null,16,pe),a("div",l({ref:"yBar",class:e.cx("bary"),tabindex:"0",role:"scrollbar","aria-orientation":"vertical","aria-controls":s.contentId,"aria-valuenow":d.lastScrollTop,onMousedown:t[7]||(t[7]=function(){return s.onYBarMouseDown&&s.onYBarMouseDown.apply(s,arguments)}),onKeydown:t[8]||(t[8]=function(r){return s.onKeyDown(r)}),onKeyup:t[9]||(t[9]=function(){return s.onKeyUp&&s.onKeyUp.apply(s,arguments)}),onFocus:t[10]||(t[10]=function(){return s.onFocus&&s.onFocus.apply(s,arguments)})},e.ptm("bary"),{"data-pc-group-section":"bar"}),null,16,me)],16)}F.render=fe;var ve=`
.p-scrolltop.p-button {
    position: fixed !important;
    inset-block-end: 20px;
    inset-inline-end: 20px;
}

.p-scrolltop-sticky.p-button {
    position: sticky !important;
    display: flex;
    margin-inline-start: auto;
}

.p-scrolltop-enter-from {
    opacity: 0;
}

.p-scrolltop-enter-active {
    transition: opacity 0.15s;
}

.p-scrolltop.p-scrolltop-leave-to {
    opacity: 0;
}

.p-scrolltop-leave-active {
    transition: opacity 0.15s;
}
`,he={root:function(t){var i=t.props;return["p-scrolltop",{"p-scrolltop-sticky":i.target!=="window"}]},icon:"p-scrolltop-icon"},ge=$.extend({name:"scrolltop",style:ve,classes:he}),be={name:"BaseScrollTop",extends:B,props:{target:{type:String,default:"window"},threshold:{type:Number,default:400},icon:{type:String,default:void 0},behavior:{type:String,default:"smooth"},buttonProps:{type:Object,default:function(){return{rounded:!0}}}},style:ge,provide:function(){return{$pcScrollTop:this,$parentInstance:this}}},R={name:"ScrollTop",extends:be,inheritAttrs:!1,scrollListener:null,container:null,data:function(){return{visible:!1}},mounted:function(){this.target==="window"?this.bindDocumentScrollListener():this.target==="parent"&&this.bindParentScrollListener()},beforeUnmount:function(){this.target==="window"?this.unbindDocumentScrollListener():this.target==="parent"&&this.unbindParentScrollListener(),this.container&&(k.clear(this.container),this.overlay=null)},methods:{onClick:function(){var t=this.target==="window"?window:this.$el.parentElement;t.scroll({top:0,behavior:this.behavior})},checkVisibility:function(t){t>this.threshold?this.visible=!0:this.visible=!1},bindParentScrollListener:function(){var t=this;this.scrollListener=function(){t.checkVisibility(t.$el.parentElement.scrollTop)},this.$el.parentElement.addEventListener("scroll",this.scrollListener)},bindDocumentScrollListener:function(){var t=this;this.scrollListener=function(){t.checkVisibility(V())},window.addEventListener("scroll",this.scrollListener)},unbindParentScrollListener:function(){this.scrollListener&&(this.$el.parentElement.removeEventListener("scroll",this.scrollListener),this.scrollListener=null)},unbindDocumentScrollListener:function(){this.scrollListener&&(window.removeEventListener("scroll",this.scrollListener),this.scrollListener=null)},onEnter:function(t){k.set("overlay",t,this.$primevue.config.zIndex.overlay)},onAfterLeave:function(t){k.clear(t)},containerRef:function(t){this.container=t?t.$el:void 0}},computed:{scrollTopAriaLabel:function(){return this.$primevue.config.locale.aria?this.$primevue.config.locale.aria.scrollTop:void 0}},components:{ChevronUpIcon:_,Button:U}};function ye(e,t,i,o,d,s){var r=A("Button");return u(),S(W,l({name:"p-scrolltop",appear:"",onEnter:s.onEnter,onAfterLeave:s.onAfterLeave},e.ptm("transition")),{default:h(function(){return[d.visible?(u(),S(r,l({key:0,ref:s.containerRef,class:e.cx("root"),onClick:s.onClick,"aria-label":s.scrollTopAriaLabel,unstyled:e.unstyled},e.buttonProps,{pt:e.pt}),{icon:h(function(b){return[x(e.$slots,"icon",{class:D(e.cx("icon"))},function(){return[(u(),S(T(e.icon?"span":"ChevronUpIcon"),l({class:[e.cx("icon"),e.icon,b.class]},e.ptm("icon")),null,16,["class"]))]})]}),_:3},16,["class","onClick","aria-label","unstyled","pt"])):C("",!0)]}),_:3},16,["onEnter","onAfterLeave"])}R.render=ye;var $e={root:"p-avatar-group p-component"},we=$.extend({name:"avatargroup",classes:$e}),Be={name:"BaseAvatarGroup",extends:B,style:we,provide:function(){return{$pcAvatarGroup:this,$parentInstance:this}}},E={name:"AvatarGroup",extends:Be,inheritAttrs:!1};function xe(e,t,i,o,d,s){return u(),f("div",l({class:e.cx("root")},e.ptmi("root")),[x(e.$slots,"default")],16)}E.render=xe;var Le=({dt:e})=>`
.p-avatar {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: ${e("avatar.width")};
    height: ${e("avatar.height")};
    font-size: ${e("avatar.font.size")};
    background: ${e("avatar.background")};
    color: ${e("avatar.color")};
    border-radius: ${e("avatar.border.radius")};
}

.p-avatar-image {
    background: transparent;
}

.p-avatar-circle {
    border-radius: 50%;
}

.p-avatar-circle img {
    border-radius: 50%;
}

.p-avatar-icon {
    font-size: ${e("avatar.icon.size")};
    width: ${e("avatar.icon.size")};
    height: ${e("avatar.icon.size")};
}

.p-avatar img {
    width: 100%;
    height: 100%;
}

.p-avatar-lg {
    width: ${e("avatar.lg.width")};
    height: ${e("avatar.lg.width")};
    font-size: ${e("avatar.lg.font.size")};
}

.p-avatar-lg .p-avatar-icon {
    font-size: ${e("avatar.lg.icon.size")};
    width: ${e("avatar.lg.icon.size")};
    height: ${e("avatar.lg.icon.size")};
}

.p-avatar-xl {
    width: ${e("avatar.xl.width")};
    height: ${e("avatar.xl.width")};
    font-size: ${e("avatar.xl.font.size")};
}

.p-avatar-xl .p-avatar-icon {
    font-size: ${e("avatar.xl.icon.size")};
    width: ${e("avatar.xl.icon.size")};
    height: ${e("avatar.xl.icon.size")};
}

.p-avatar-group {
    display: flex;
    align-items: center;
}

.p-avatar-group .p-avatar + .p-avatar {
    margin-inline-start: ${e("avatar.group.offset")};
}

.p-avatar-group .p-avatar {
    border: 2px solid ${e("avatar.group.border.color")};
}

.p-avatar-group .p-avatar-lg + .p-avatar-lg {
    margin-inline-start: ${e("avatar.lg.group.offset")};
}

.p-avatar-group .p-avatar-xl + .p-avatar-xl {
    margin-inline-start: ${e("avatar.xl.group.offset")};
}
`,Se={root:function(t){var i=t.props;return["p-avatar p-component",{"p-avatar-image":i.image!=null,"p-avatar-circle":i.shape==="circle","p-avatar-lg":i.size==="large","p-avatar-xl":i.size==="xlarge"}]},label:"p-avatar-label",icon:"p-avatar-icon"},ke=$.extend({name:"avatar",style:Le,classes:Se}),ze={name:"BaseAvatar",extends:B,props:{label:{type:String,default:null},icon:{type:String,default:null},image:{type:String,default:null},size:{type:String,default:"normal"},shape:{type:String,default:"square"},ariaLabelledby:{type:String,default:null},ariaLabel:{type:String,default:null}},style:ke,provide:function(){return{$pcAvatar:this,$parentInstance:this}}},X={name:"Avatar",extends:ze,inheritAttrs:!1,emits:["error"],methods:{onError:function(t){this.$emit("error",t)}}},Me=["aria-labelledby","aria-label"],Ae=["src","alt"];function De(e,t,i,o,d,s){return u(),f("div",l({class:e.cx("root"),"aria-labelledby":e.ariaLabelledby,"aria-label":e.ariaLabel},e.ptmi("root")),[x(e.$slots,"default",{},function(){return[e.label?(u(),f("span",l({key:0,class:e.cx("label")},e.ptm("label")),G(e.label),17)):e.$slots.icon?(u(),S(T(e.$slots.icon),{key:1,class:D(e.cx("icon"))},null,8,["class"])):e.icon?(u(),f("span",l({key:2,class:[e.cx("icon"),e.icon]},e.ptm("icon")),null,16)):e.image?(u(),f("img",l({key:3,src:e.image,alt:e.ariaLabel,onError:t[0]||(t[0]=function(){return s.onError&&s.onError.apply(s,arguments)})},e.ptm("image")),null,16,Ae)):C("",!0)]})],16,Me)}X.render=De;var Te=({dt:e})=>`
.p-overlaybadge {
    position: relative;
}

.p-overlaybadge .p-badge {
    position: absolute;
    inset-block-start: 0;
    inset-inline-end: 0;
    transform: translate(50%, -50%);
    transform-origin: 100% 0;
    margin: 0;
    outline-width: ${e("overlaybadge.outline.width")};
    outline-style: solid;
    outline-color: ${e("overlaybadge.outline.color")};
}

.p-overlaybadge .p-badge:dir(rtl) {
    transform: translate(-50%, -50%);
}
`,Ce={root:"p-overlaybadge"},Ue=$.extend({name:"overlaybadge",style:Te,classes:Ce}),Pe={name:"OverlayBadge",extends:z,style:Ue,provide:function(){return{$pcOverlayBadge:this,$parentInstance:this}}},I={name:"OverlayBadge",extends:Pe,inheritAttrs:!1,components:{Badge:z}};function Fe(e,t,i,o,d,s){var r=A("Badge");return u(),f("div",l({class:e.cx("root")},e.ptmi("root")),[x(e.$slots,"default"),n(r,l(e.$props,{pt:e.ptm("pcBadge")}),null,16,["pt"])],16)}I.render=Fe;const Re={class:"card"},Ee={class:"flex flex-col md:flex-row gap-4"},Xe={class:"md:w-1/2"},Ie={class:"md:w-1/2"},Ye={class:"flex flex-col md:flex-row gap-8"},qe={class:"md:w-1/2"},He={class:"card"},Ke={class:"flex gap-2"},Ve={class:"flex gap-6"},We={class:"flex gap-2"},Ge={class:"flex items-start gap-2"},Ne={class:"card"},Oe={class:"card"},je={class:"md:w-1/2"},Je={class:"card"},Ze={class:"flex gap-2"},Qe={class:"flex gap-2"},_e={class:"flex gap-2"},et={class:"card"},tt={class:"flex items-center flex-col sm:flex-row"},nt={class:"flex items-center flex-col sm:flex-row"},rt={class:"flex items-center flex-col sm:flex-row"},st={class:"card"},at={class:"rounded-border border border-surface p-6"},it={class:"flex mb-4"},ot={class:"flex justify-between mt-4"},vt={__name:"MiscDoc",setup(e){const t=N(0);let i=null;function o(){i=setInterval(()=>{let s=t.value+Math.floor(Math.random()*10)+1;s>=100&&(s=100),t.value=s},2e3)}function d(){clearInterval(i),i=null}return O(()=>{o()}),j(()=>{d()}),(s,r)=>{const b=ee,v=z,L=I,M=U,m=X,Y=E,q=R,H=F,c=Q,p=Z,g=P;return u(),f(J,null,[a("div",Re,[r[0]||(r[0]=a("div",{class:"font-semibold text-xl mb-4"},"ProgressBar",-1)),a("div",Ee,[a("div",Xe,[n(b,{value:t.value},null,8,["value"])]),a("div",Ie,[n(b,{value:50,showValue:!1})])])]),a("div",Ye,[a("div",qe,[a("div",He,[r[4]||(r[4]=a("div",{class:"font-semibold text-xl mb-4"},"Badge",-1)),a("div",Ke,[n(v,{value:2}),n(v,{value:8,severity:"success"}),n(v,{value:4,severity:"info"}),n(v,{value:12,severity:"Warn"}),n(v,{value:3,severity:"danger"})]),r[5]||(r[5]=a("div",{class:"font-semibold my-4"},"Overlay",-1)),a("div",Ve,[n(L,{value:"2"},{default:h(()=>r[1]||(r[1]=[a("i",{class:"pi pi-bell",style:{"font-size":"2rem"}},null,-1)])),_:1}),n(L,{value:"4",severity:"danger"},{default:h(()=>r[2]||(r[2]=[a("i",{class:"pi pi-calendar",style:{"font-size":"2rem"}},null,-1)])),_:1}),n(L,{severity:"danger"},{default:h(()=>r[3]||(r[3]=[a("i",{class:"pi pi-envelope",style:{"font-size":"2rem"}},null,-1)])),_:1})]),r[6]||(r[6]=a("div",{class:"font-semibold my-4"},"Button",-1)),a("div",We,[n(M,{label:"Emails",badge:"8",class:"mr-2"}),n(M,{label:"Messages",icon:"pi pi-users",severity:"warn",badge:"8",badgeClass:"p-badge-danger"})]),r[7]||(r[7]=a("div",{class:"font-semibold my-4"},"Sizes",-1)),a("div",Ge,[n(v,{value:2}),n(v,{value:4,size:"large",severity:"warn"}),n(v,{value:6,size:"xlarge",severity:"success"})])]),a("div",Ne,[r[8]||(r[8]=a("div",{class:"font-semibold text-xl mb-4"},"Avatar",-1)),r[9]||(r[9]=a("div",{class:"font-semibold mb-4"},"Group",-1)),n(Y,null,{default:h(()=>[n(m,{image:"https://primefaces.org/cdn/primevue/images/avatar/amyelsner.png",size:"large",shape:"circle"}),n(m,{image:"https://primefaces.org/cdn/primevue/images/avatar/asiyajavayant.png",size:"large",shape:"circle"}),n(m,{image:"https://primefaces.org/cdn/primevue/images/avatar/onyamalimba.png",size:"large",shape:"circle"}),n(m,{image:"https://primefaces.org/cdn/primevue/images/avatar/ionibowcher.png",size:"large",shape:"circle"}),n(m,{image:"https://primefaces.org/cdn/primevue/images/avatar/xuxuefeng.png",size:"large",shape:"circle"}),n(m,{label:"+2",shape:"circle",size:"large",style:{"background-color":"#9c27b0",color:"#ffffff"}})]),_:1}),r[10]||(r[10]=a("div",{class:"font-semibold my-4"},"Label - Circle",-1)),n(m,{label:"P",class:"mr-2",size:"xlarge",shape:"circle"}),n(m,{label:"V",class:"mr-2",size:"large",style:{"background-color":"#2196F3",color:"#ffffff"},shape:"circle"}),n(m,{label:"U",class:"mr-2",style:{"background-color":"#9c27b0",color:"#ffffff"},shape:"circle"}),r[11]||(r[11]=a("div",{class:"font-semibold my-4"},"Icon - Badge",-1)),n(L,{value:"4",severity:"danger",class:"inline-flex"},{default:h(()=>[n(m,{label:"U",size:"xlarge"})]),_:1})]),a("div",Oe,[r[13]||(r[13]=a("div",{class:"font-semibold text-xl mb-4"},"ScrollTop",-1)),n(H,{style:{width:"250px",height:"200px"}},{default:h(()=>[r[12]||(r[12]=a("p",null," Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Vitae et leo duis ut diam. Ultricies mi quis hendrerit dolor magna eget est lorem. Amet consectetur adipiscing elit ut. Nam libero justo laoreet sit amet. Pharetra massa massa ultricies mi quis hendrerit dolor magna. Est ultricies integer quis auctor elit sed vulputate. Consequat ac felis donec et. Tellus orci ac auctor augue mauris. Semper feugiat nibh sed pulvinar proin gravida hendrerit lectus a. Tincidunt arcu non sodales neque sodales. Metus aliquam eleifend mi in nulla posuere sollicitudin aliquam ultrices. Sodales ut etiam sit amet nisl purus. Cursus sit amet dictum sit amet. Tristique senectus et netus et malesuada fames ac turpis egestas. Et tortor consequat id porta nibh venenatis cras sed. Diam maecenas ultricies mi eget mauris. Eget egestas purus viverra accumsan in nisl nisi. Suscipit adipiscing bibendum est ultricies integer. Mattis aliquam faucibus purus in massa tempor nec. ",-1)),n(q,{target:"parent",threshold:100,icon:"pi pi-arrow-up"})]),_:1})])]),a("div",je,[a("div",Je,[r[14]||(r[14]=a("div",{class:"font-semibold text-xl mb-4"},"Tag",-1)),r[15]||(r[15]=a("div",{class:"font-semibold mb-4"},"Default",-1)),a("div",Ze,[n(c,{value:"Primary"}),n(c,{severity:"success",value:"Success"}),n(c,{severity:"info",value:"Info"}),n(c,{severity:"warn",value:"Warn"}),n(c,{severity:"danger",value:"Danger"})]),r[16]||(r[16]=a("div",{class:"font-semibold my-4"},"Pills",-1)),a("div",Qe,[n(c,{value:"Primary",rounded:!0}),n(c,{severity:"success",value:"Success",rounded:!0}),n(c,{severity:"info",value:"Info",rounded:!0}),n(c,{severity:"warn",value:"Warn",rounded:!0}),n(c,{severity:"danger",value:"Danger",rounded:!0})]),r[17]||(r[17]=a("div",{class:"font-semibold my-4"},"Icons",-1)),a("div",_e,[n(c,{icon:"pi pi-user",value:"Primary"}),n(c,{icon:"pi pi-check",severity:"success",value:"Success"}),n(c,{icon:"pi pi-info-circle",severity:"info",value:"Info"}),n(c,{con:"pi pi-exclamation-triangle",severity:"warn",value:"Warn"}),n(c,{icon:"pi pi-times",severity:"danger",value:"Danger"})])]),a("div",et,[r[18]||(r[18]=a("div",{class:"font-semibold text-xl mb-4"},"Chip",-1)),r[19]||(r[19]=a("div",{class:"font-semibold mb-4"},"Basic",-1)),a("div",tt,[n(p,{label:"Action",class:"mr-2 mb-2"}),n(p,{label:"Comedy",class:"mr-2 mb-2"}),n(p,{label:"Mystery",class:"mr-2 mb-2"}),n(p,{label:"Thriller",removable:!0,class:"mb-2"})]),r[20]||(r[20]=a("div",{class:"font-semibold my-4"},"Icon",-1)),a("div",nt,[n(p,{label:"Apple",icon:"pi pi-apple",class:"mr-2 mb-2"}),n(p,{label:"Facebook",icon:"pi pi-facebook",class:"mr-2 mb-2"}),n(p,{label:"Google",icon:"pi pi-google",class:"mr-2 mb-2"}),n(p,{label:"Microsoft",icon:"pi pi-microsoft",removable:!0,class:"mb-2"})]),r[21]||(r[21]=a("div",{class:"font-semibold my-4"},"Image",-1)),a("div",rt,[n(p,{label:"Amy Elsner",image:"https://primefaces.org/cdn/primevue/images/avatar/amyelsner.png",class:"mr-2 mb-2"}),n(p,{label:"Asiya Javayant",image:"https://primefaces.org/cdn/primevue/images/avatar/asiyajavayant.png",class:"mr-2 mb-2"}),n(p,{label:"Onyama Limba",image:"https://primefaces.org/cdn/primevue/images/avatar/onyamalimba.png",class:"mr-2 mb-2"})])]),a("div",st,[r[22]||(r[22]=a("div",{class:"font-semibold text-xl mb-4"},"Skeleton",-1)),a("div",at,[a("div",it,[n(g,{shape:"circle",size:"4rem",class:"mr-2"}),a("div",null,[n(g,{width:"10rem",class:"mb-2"}),n(g,{width:"5rem",class:"mb-2"}),n(g,{height:".5rem"})])]),n(g,{width:"100%",height:"150px"}),a("div",ot,[n(g,{width:"4rem",height:"2rem"}),n(g,{width:"4rem",height:"2rem"})])])])])])],64)}}};export{vt as default};
