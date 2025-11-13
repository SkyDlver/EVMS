import{B as e,s as t,o,c as s,i as r,m as l}from"./index-Cjkixd6k.js";var f=({dt:n})=>`
.p-iconfield {
    position: relative;
}

.p-inputicon {
    position: absolute;
    top: 50%;
    margin-top: calc(-1 * (${n("icon.size")} / 2));
    color: ${n("iconfield.icon.color")};
    line-height: 1;
    z-index: 1;
}

.p-iconfield .p-inputicon:first-child {
    inset-inline-start: ${n("form.field.padding.x")};
}

.p-iconfield .p-inputicon:last-child {
    inset-inline-end: ${n("form.field.padding.x")};
}

.p-iconfield .p-inputtext:not(:first-child),
.p-iconfield .p-inputwrapper:not(:first-child) .p-inputtext {
    padding-inline-start: calc((${n("form.field.padding.x")} * 2) + ${n("icon.size")});
}

.p-iconfield .p-inputtext:not(:last-child) {
    padding-inline-end: calc((${n("form.field.padding.x")} * 2) + ${n("icon.size")});
}

.p-iconfield:has(.p-inputfield-sm) .p-inputicon {
    font-size: ${n("form.field.sm.font.size")};
    width: ${n("form.field.sm.font.size")};
    height: ${n("form.field.sm.font.size")};
    margin-top: calc(-1 * (${n("form.field.sm.font.size")} / 2));
}

.p-iconfield:has(.p-inputfield-lg) .p-inputicon {
    font-size: ${n("form.field.lg.font.size")};
    width: ${n("form.field.lg.font.size")};
    height: ${n("form.field.lg.font.size")};
    margin-top: calc(-1 * (${n("form.field.lg.font.size")} / 2));
}
`,$={root:"p-iconfield"},m=e.extend({name:"iconfield",style:f,classes:$}),u={name:"BaseIconField",extends:t,style:m,provide:function(){return{$pcIconField:this,$parentInstance:this}}},h={name:"IconField",extends:u,inheritAttrs:!1};function g(n,p,a,c,d,i){return o(),s("div",l({class:n.cx("root")},n.ptmi("root")),[r(n.$slots,"default")],16)}h.render=g;var v={root:"p-inputicon"},x=e.extend({name:"inputicon",classes:v}),z={name:"BaseInputIcon",extends:t,style:x,props:{class:null},provide:function(){return{$pcInputIcon:this,$parentInstance:this}}},I={name:"InputIcon",extends:z,inheritAttrs:!1,computed:{containerClass:function(){return[this.cx("root"),this.class]}}};function y(n,p,a,c,d,i){return o(),s("span",l({class:i.containerClass},n.ptmi("root")),[r(n.$slots,"default")],16)}I.render=y;export{h as a,I as s};
