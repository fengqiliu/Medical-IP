const __vite__mapDeps=(i,m=__vite__mapDeps,d=(m.f||(m.f=["assets/index-BbLZ5P7D.js","assets/vendor-CSj9hcp_.js","assets/index-Bd8g9rLw.js","assets/antd-C0pnNR-A.js","assets/index-DYoibeTn.js","assets/patient-mx5b0GGp.js","assets/index-DWkQvzl-.js","assets/index-BXuHDRNt.js","assets/PatientHeader-CN4lP8q0.js","assets/AISummaryCard-aPTQjga6.js","assets/index-Doz38uUw.js","assets/index-Bml7pWNB.js","assets/ai-BHUQYugo.js","assets/index-D8tAifjj.js","assets/index-2Bm9Uafs.js"])))=>i.map(i=>d[i]);
import{r as m,c as ce,g as U,R as q,u as W,f as ue,O as de,h as pe,i as x,N as B,B as fe}from"./vendor-CSj9hcp_.js";import{L as _,R as me,a as ve,D as xe,b as he,M as be,c as ye,d as ge,e as _e,S as je,C as Se}from"./antd-C0pnNR-A.js";(function(){const t=document.createElement("link").relList;if(t&&t.supports&&t.supports("modulepreload"))return;for(const r of document.querySelectorAll('link[rel="modulepreload"]'))a(r);new MutationObserver(r=>{for(const s of r)if(s.type==="childList")for(const i of s.addedNodes)i.tagName==="LINK"&&i.rel==="modulepreload"&&a(i)}).observe(document,{childList:!0,subtree:!0});function n(r){const s={};return r.integrity&&(s.integrity=r.integrity),r.referrerPolicy&&(s.referrerPolicy=r.referrerPolicy),r.crossOrigin==="use-credentials"?s.credentials="include":r.crossOrigin==="anonymous"?s.credentials="omit":s.credentials="same-origin",s}function a(r){if(r.ep)return;r.ep=!0;const s=n(r);fetch(r.href,s)}})();var K={exports:{}},E={};/**
 * @license React
 * react-jsx-runtime.production.min.js
 *
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */var Ee=m,Pe=Symbol.for("react.element"),$e=Symbol.for("react.fragment"),we=Object.prototype.hasOwnProperty,Oe=Ee.__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED.ReactCurrentOwner,ke={key:!0,ref:!0,__self:!0,__source:!0};function G(e,t,n){var a,r={},s=null,i=null;n!==void 0&&(s=""+n),t.key!==void 0&&(s=""+t.key),t.ref!==void 0&&(i=t.ref);for(a in t)we.call(t,a)&&!ke.hasOwnProperty(a)&&(r[a]=t[a]);if(e&&e.defaultProps)for(a in t=e.defaultProps,t)r[a]===void 0&&(r[a]=t[a]);return{$$typeof:Pe,type:e,key:s,ref:i,props:r,_owner:Oe.current}}E.Fragment=$e;E.jsx=G;E.jsxs=G;K.exports=E;var o=K.exports,N={},L=ce;N.createRoot=L.createRoot,N.hydrateRoot=L.hydrateRoot;var P={},H={exports:{}};(function(e){function t(n){return n&&n.__esModule?n:{default:n}}e.exports=t,e.exports.__esModule=!0,e.exports.default=e.exports})(H);var $=H.exports,w={};Object.defineProperty(w,"__esModule",{value:!0});w.default=void 0;var Re={items_per_page:"条/页",jump_to:"跳至",jump_to_confirm:"确定",page:"页",prev_page:"上一页",next_page:"下一页",prev_5:"向前 5 页",next_5:"向后 5 页",prev_3:"向前 3 页",next_3:"向后 3 页",page_size:"页码"};w.default=Re;var O={},j={},k={},Q={exports:{}},J={exports:{}},X={exports:{}},Z={exports:{}};(function(e){function t(n){"@babel/helpers - typeof";return e.exports=t=typeof Symbol=="function"&&typeof Symbol.iterator=="symbol"?function(a){return typeof a}:function(a){return a&&typeof Symbol=="function"&&a.constructor===Symbol&&a!==Symbol.prototype?"symbol":typeof a},e.exports.__esModule=!0,e.exports.default=e.exports,t(n)}e.exports=t,e.exports.__esModule=!0,e.exports.default=e.exports})(Z);var ee=Z.exports,te={exports:{}};(function(e){var t=ee.default;function n(a,r){if(t(a)!="object"||!a)return a;var s=a[Symbol.toPrimitive];if(s!==void 0){var i=s.call(a,r||"default");if(t(i)!="object")return i;throw new TypeError("@@toPrimitive must return a primitive value.")}return(r==="string"?String:Number)(a)}e.exports=n,e.exports.__esModule=!0,e.exports.default=e.exports})(te);var Ce=te.exports;(function(e){var t=ee.default,n=Ce;function a(r){var s=n(r,"string");return t(s)=="symbol"?s:s+""}e.exports=a,e.exports.__esModule=!0,e.exports.default=e.exports})(X);var De=X.exports;(function(e){var t=De;function n(a,r,s){return(r=t(r))in a?Object.defineProperty(a,r,{value:s,enumerable:!0,configurable:!0,writable:!0}):a[r]=s,a}e.exports=n,e.exports.__esModule=!0,e.exports.default=e.exports})(J);var Ie=J.exports;(function(e){var t=Ie;function n(r,s){var i=Object.keys(r);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(r);s&&(l=l.filter(function(c){return Object.getOwnPropertyDescriptor(r,c).enumerable})),i.push.apply(i,l)}return i}function a(r){for(var s=1;s<arguments.length;s++){var i=arguments[s]!=null?arguments[s]:{};s%2?n(Object(i),!0).forEach(function(l){t(r,l,i[l])}):Object.getOwnPropertyDescriptors?Object.defineProperties(r,Object.getOwnPropertyDescriptors(i)):n(Object(i)).forEach(function(l){Object.defineProperty(r,l,Object.getOwnPropertyDescriptor(i,l))})}return r}e.exports=a,e.exports.__esModule=!0,e.exports.default=e.exports})(Q);var Ne=Q.exports,R={};Object.defineProperty(R,"__esModule",{value:!0});R.commonLocale=void 0;R.commonLocale={yearFormat:"YYYY",dayFormat:"D",cellMeridiemFormat:"A",monthBeforeYear:!0};var ze=$.default;Object.defineProperty(k,"__esModule",{value:!0});k.default=void 0;var A=ze(Ne),Te=R,Le=(0,A.default)((0,A.default)({},Te.commonLocale),{},{locale:"zh_CN",today:"今天",now:"此刻",backToToday:"返回今天",ok:"确定",timeSelect:"选择时间",dateSelect:"选择日期",weekSelect:"选择周",clear:"清除",week:"周",month:"月",year:"年",previousMonth:"上个月 (翻页上键)",nextMonth:"下个月 (翻页下键)",monthSelect:"选择月份",yearSelect:"选择年份",decadeSelect:"选择年代",previousYear:"上一年 (Control键加左方向键)",nextYear:"下一年 (Control键加右方向键)",previousDecade:"上一年代",nextDecade:"下一年代",previousCentury:"上一世纪",nextCentury:"下一世纪",yearFormat:"YYYY年",cellDateFormat:"D",monthBeforeYear:!1});k.default=Le;var S={};Object.defineProperty(S,"__esModule",{value:!0});S.default=void 0;const Ae={placeholder:"请选择时间",rangePlaceholder:["开始时间","结束时间"]};S.default=Ae;var re=$.default;Object.defineProperty(j,"__esModule",{value:!0});j.default=void 0;var Me=re(k),Ve=re(S);const oe={lang:Object.assign({placeholder:"请选择日期",yearPlaceholder:"请选择年份",quarterPlaceholder:"请选择季度",monthPlaceholder:"请选择月份",weekPlaceholder:"请选择周",rangePlaceholder:["开始日期","结束日期"],rangeYearPlaceholder:["开始年份","结束年份"],rangeMonthPlaceholder:["开始月份","结束月份"],rangeQuarterPlaceholder:["开始季度","结束季度"],rangeWeekPlaceholder:["开始周","结束周"]},Me.default),timePickerLocale:Object.assign({},Ve.default)};oe.lang.ok="确定";j.default=oe;var Fe=$.default;Object.defineProperty(O,"__esModule",{value:!0});O.default=void 0;var Ye=Fe(j);O.default=Ye.default;var C=$.default;Object.defineProperty(P,"__esModule",{value:!0});P.default=void 0;var Ue=C(w),qe=C(O),We=C(j),Be=C(S);const f="${label}不是一个有效的${type}",Ke={locale:"zh-cn",Pagination:Ue.default,DatePicker:We.default,TimePicker:Be.default,Calendar:qe.default,global:{placeholder:"请选择",close:"关闭"},Table:{filterTitle:"筛选",filterConfirm:"确定",filterReset:"重置",filterEmptyText:"无筛选项",filterCheckAll:"全选",filterSearchPlaceholder:"在筛选项中搜索",emptyText:"暂无数据",selectAll:"全选当页",selectInvert:"反选当页",selectNone:"清空所有",selectionAll:"全选所有",sortTitle:"排序",expand:"展开行",collapse:"关闭行",triggerDesc:"点击降序",triggerAsc:"点击升序",cancelSort:"取消排序"},Modal:{okText:"确定",cancelText:"取消",justOkText:"知道了"},Tour:{Next:"下一步",Previous:"上一步",Finish:"结束导览"},Popconfirm:{cancelText:"取消",okText:"确定"},Transfer:{titles:["",""],searchPlaceholder:"请输入搜索内容",itemUnit:"项",itemsUnit:"项",remove:"删除",selectCurrent:"全选当页",removeCurrent:"删除当页",selectAll:"全选所有",deselectAll:"取消全选",removeAll:"删除全部",selectInvert:"反选当页"},Upload:{uploading:"文件上传中",removeFile:"删除文件",uploadError:"上传错误",previewFile:"预览文件",downloadFile:"下载文件"},Empty:{description:"暂无数据"},Icon:{icon:"图标"},Text:{edit:"编辑",copy:"复制",copied:"复制成功",expand:"展开",collapse:"收起"},Form:{optional:"（可选）",defaultValidateMessages:{default:"字段验证错误${label}",required:"请输入${label}",enum:"${label}必须是其中一个[${enum}]",whitespace:"${label}不能为空字符",date:{format:"${label}日期格式无效",parse:"${label}不能转换为日期",invalid:"${label}是一个无效日期"},types:{string:f,method:f,array:f,object:f,number:f,date:f,boolean:f,integer:f,float:f,regexp:f,email:f,url:f,hex:f},string:{len:"${label}须为${len}个字符",min:"${label}最少${min}个字符",max:"${label}最多${max}个字符",range:"${label}须在${min}-${max}字符之间"},number:{len:"${label}必须等于${len}",min:"${label}最小值为${min}",max:"${label}最大值为${max}",range:"${label}须在${min}-${max}之间"},array:{len:"须为${len}个${label}",min:"最少${min}个${label}",max:"最多${max}个${label}",range:"${label}数量须在${min}-${max}之间"},pattern:{mismatch:"${label}与模式不匹配${pattern}"}}},Image:{preview:"预览"},QRCode:{expired:"二维码过期",refresh:"点击刷新",scanned:"已扫描"},ColorPicker:{presetEmpty:"暂无",transparent:"无色",singleColor:"单色",gradientColor:"渐变色"}};P.default=Ke;var Ge=P;const He=U(Ge),Qe="modulepreload",Je=function(e){return"/"+e},M={},y=function(t,n,a){let r=Promise.resolve();if(n&&n.length>0){document.getElementsByTagName("link");const i=document.querySelector("meta[property=csp-nonce]"),l=(i==null?void 0:i.nonce)||(i==null?void 0:i.getAttribute("nonce"));r=Promise.allSettled(n.map(c=>{if(c=Je(c),c in M)return;M[c]=!0;const h=c.endsWith(".css"),d=h?'[rel="stylesheet"]':"";if(document.querySelector(`link[href="${c}"]${d}`))return;const u=document.createElement("link");if(u.rel=h?"stylesheet":Qe,h||(u.as="script"),u.crossOrigin="",u.href=c,l&&u.setAttribute("nonce",l),document.head.appendChild(u),h)return new Promise((v,p)=>{u.addEventListener("load",v),u.addEventListener("error",()=>p(new Error(`Unable to preload CSS for ${c}`)))})}))}function s(i){const l=new Event("vite:preloadError",{cancelable:!0});if(l.payload=i,window.dispatchEvent(l),!l.defaultPrevented)throw i}return r.then(i=>{for(const l of i||[])l.status==="rejected"&&s(l.reason);return t().catch(s)})},Xe={},V=e=>{let t;const n=new Set,a=(d,u)=>{const v=typeof d=="function"?d(t):d;if(!Object.is(v,t)){const p=t;t=u??(typeof v!="object"||v===null)?v:Object.assign({},t,v),n.forEach(b=>b(t,p))}},r=()=>t,c={setState:a,getState:r,getInitialState:()=>h,subscribe:d=>(n.add(d),()=>n.delete(d)),destroy:()=>{(Xe?"production":void 0)!=="production"&&console.warn("[DEPRECATED] The `destroy` method will be unsupported in a future version. Instead use unsubscribe function returned by subscribe. Everything will be garbage-collected if store is garbage-collected."),n.clear()}},h=t=e(a,r,c);return c},Ze=e=>e?V(e):V;var ae={exports:{}},ne={},se={exports:{}},ie={};/**
 * @license React
 * use-sync-external-store-shim.production.js
 *
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */var g=m;function et(e,t){return e===t&&(e!==0||1/e===1/t)||e!==e&&t!==t}var tt=typeof Object.is=="function"?Object.is:et,rt=g.useState,ot=g.useEffect,at=g.useLayoutEffect,nt=g.useDebugValue;function st(e,t){var n=t(),a=rt({inst:{value:n,getSnapshot:t}}),r=a[0].inst,s=a[1];return at(function(){r.value=n,r.getSnapshot=t,I(r)&&s({inst:r})},[e,n,t]),ot(function(){return I(r)&&s({inst:r}),e(function(){I(r)&&s({inst:r})})},[e]),nt(n),n}function I(e){var t=e.getSnapshot;e=e.value;try{var n=t();return!tt(e,n)}catch{return!0}}function it(e,t){return t()}var lt=typeof window>"u"||typeof window.document>"u"||typeof window.document.createElement>"u"?it:st;ie.useSyncExternalStore=g.useSyncExternalStore!==void 0?g.useSyncExternalStore:lt;se.exports=ie;var ct=se.exports;/**
 * @license React
 * use-sync-external-store-shim/with-selector.production.js
 *
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */var D=m,ut=ct;function dt(e,t){return e===t&&(e!==0||1/e===1/t)||e!==e&&t!==t}var pt=typeof Object.is=="function"?Object.is:dt,ft=ut.useSyncExternalStore,mt=D.useRef,vt=D.useEffect,xt=D.useMemo,ht=D.useDebugValue;ne.useSyncExternalStoreWithSelector=function(e,t,n,a,r){var s=mt(null);if(s.current===null){var i={hasValue:!1,value:null};s.current=i}else i=s.current;s=xt(function(){function c(p){if(!h){if(h=!0,d=p,p=a(p),r!==void 0&&i.hasValue){var b=i.value;if(r(b,p))return u=b}return u=p}if(b=u,pt(d,p))return b;var T=a(p);return r!==void 0&&r(b,T)?(d=p,b):(d=p,u=T)}var h=!1,d,u,v=n===void 0?null:n;return[function(){return c(t())},v===null?void 0:function(){return c(v())}]},[t,n,a,r]);var l=ft(e,s[0],s[1]);return vt(function(){i.hasValue=!0,i.value=l},[l]),ht(l),l};ae.exports=ne;var bt=ae.exports;const yt=U(bt),le={},{useDebugValue:gt}=q,{useSyncExternalStoreWithSelector:_t}=yt;let F=!1;const jt=e=>e;function St(e,t=jt,n){(le?"production":void 0)!=="production"&&n&&!F&&(console.warn("[DEPRECATED] Use `createWithEqualityFn` instead of `create` or use `useStoreWithEqualityFn` instead of `useStore`. They can be imported from 'zustand/traditional'. https://github.com/pmndrs/zustand/discussions/1937"),F=!0);const a=_t(e.subscribe,e.getState,e.getServerState||e.getInitialState,t,n);return gt(a),a}const Y=e=>{(le?"production":void 0)!=="production"&&typeof e!="function"&&console.warn("[DEPRECATED] Passing a vanilla store will be unsupported in a future version. Instead use `import { useStore } from 'zustand'`.");const t=typeof e=="function"?Ze(e):e,n=(a,r)=>St(t,a,r);return Object.assign(n,t),n},Et=e=>e?Y(e):Y,z=Et(e=>({token:localStorage.getItem("token"),user:null,setToken:t=>{t?localStorage.setItem("token",t):localStorage.removeItem("token"),e({token:t})},setUser:t=>e({user:t}),logout:()=>{localStorage.removeItem("token"),e({token:null,user:null})},isLoggedIn:()=>!!z.getState().token})),{Header:Pt}=_;function $t({collapsed:e,onToggle:t}){var l;const n=W(),{user:a,logout:r}=z(),s=()=>{r(),n("/login")},i=[{key:"logout",icon:o.jsx(he,{}),label:"退出登录",onClick:s}];return o.jsxs(Pt,{className:"claude-header",children:[o.jsx("button",{className:"menu-toggle",onClick:t,children:e?o.jsx(me,{style:{fontSize:18}}):o.jsx(ve,{style:{fontSize:18}})}),o.jsxs("div",{className:"header-breadcrumb",children:[o.jsx("span",{className:"breadcrumb-item",children:"医技360"}),o.jsx("span",{className:"breadcrumb-separator",children:"/"}),o.jsx("span",{className:"breadcrumb-item active",children:"工作台"})]}),o.jsx("div",{className:"header-spacer"}),o.jsx(xe,{menu:{items:i},placement:"bottomRight",trigger:["click"],children:o.jsxs("button",{className:"user-profile-button",children:[o.jsx("div",{className:"user-avatar",children:((l=a==null?void 0:a.name)==null?void 0:l.charAt(0))||"U"}),o.jsx("span",{className:"user-name",children:(a==null?void 0:a.name)||"用户"}),o.jsx("svg",{className:"dropdown-arrow",width:"12",height:"12",viewBox:"0 0 12 12",fill:"none",children:o.jsx("path",{d:"M3 4.5L6 7.5L9 4.5",stroke:"currentColor",strokeWidth:"1.5",strokeLinecap:"round",strokeLinejoin:"round"})})]})}),o.jsx("style",{children:`
        .claude-header {
          display: flex;
          align-items: center;
          padding: 0 24px;
          height: 72px;
          background: var(--ivory) !important;
          border-bottom: 1px solid var(--border-cream);
          margin: 0;
        }

        .menu-toggle {
          width: 44px;
          height: 44px;
          display: flex;
          align-items: center;
          justify-content: center;
          background: transparent;
          border: 1px solid var(--border-warm);
          border-radius: 10px;
          cursor: pointer;
          color: var(--charcoal-warm);
          transition: all 0.2s ease;
        }

        .menu-toggle:hover {
          background: var(--warm-sand);
          border-color: var(--border-warm);
        }

        .header-breadcrumb {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-left: 20px;
        }

        .breadcrumb-item {
          font-size: 15px;
          color: var(--stone-gray);
        }

        .breadcrumb-item.active {
          color: var(--anthropic-near-black);
          font-weight: 500;
        }

        .breadcrumb-separator {
          color: var(--stone-gray);
          font-size: 14px;
        }

        .header-spacer {
          flex: 1;
        }

        .user-profile-button {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 8px 16px 8px 8px;
          background: transparent;
          border: 1px solid var(--border-warm);
          border-radius: 12px;
          cursor: pointer;
          transition: all 0.2s ease;
        }

        .user-profile-button:hover {
          background: var(--warm-sand);
          border-color: var(--border-warm);
        }

        .user-avatar {
          width: 36px;
          height: 36px;
          background: var(--anthropic-near-black);
          color: var(--ivory);
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-family: Georgia, serif;
          font-size: 14px;
          font-weight: 500;
        }

        .user-name {
          font-size: 15px;
          font-weight: 500;
          color: var(--charcoal-warm);
        }

        .dropdown-arrow {
          color: var(--stone-gray);
          margin-left: 4px;
        }

        @media (max-width: 640px) {
          .header-breadcrumb {
            display: none;
          }
          .user-name {
            display: none;
          }
        }
      `})]})}const{Sider:wt}=_;function Ot({collapsed:e}){const t=W(),n=ue(),a=[{key:"/dashboard",icon:o.jsx(ye,{}),label:"工作台"},{key:"/patient-search",icon:o.jsx(ge,{}),label:"患者360"},{key:"/system",icon:o.jsx(_e,{}),label:"系统管理"}];return o.jsxs(wt,{trigger:null,collapsible:!0,collapsed:e,className:"claude-sidebar",width:260,collapsedWidth:80,children:[o.jsxs("div",{className:"sidebar-header",children:[o.jsx("div",{className:"sidebar-logo",children:o.jsx("span",{className:"logo-icon",children:"360"})}),!e&&o.jsx("span",{className:"sidebar-title",children:"医技360"})]}),o.jsx(be,{theme:"dark",mode:"inline",selectedKeys:[n.pathname],items:a,onClick:({key:r})=>t(r),className:"claude-menu"}),o.jsx("style",{children:`
        .claude-sidebar {
          background: var(--anthropic-near-black) !important;
          border-right: none;
        }

        .claude-sidebar .ant-layout-sider-children {
          display: flex;
          flex-direction: column;
        }

        .sidebar-header {
          height: 72px;
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 0 24px;
          border-bottom: 1px solid var(--dark-surface);
        }

        .sidebar-logo {
          width: 40px;
          height: 40px;
          background: var(--terracotta-brand);
          border-radius: 10px;
          display: flex;
          align-items: center;
          justify-content: center;
        }

        .logo-icon {
          font-family: Georgia, serif;
          font-size: 16px;
          font-weight: 500;
          color: var(--ivory);
        }

        .sidebar-title {
          font-family: Georgia, serif;
          font-size: 20px;
          font-weight: 500;
          color: var(--ivory);
        }

        .claude-menu {
          background: transparent !important;
          border: none;
          padding: 16px 12px;
        }

        .claude-menu .ant-menu-item {
          color: var(--warm-silver);
          border-radius: 10px;
          margin-bottom: 4px;
          height: 48px;
          line-height: 48px;
          padding: 0 16px !important;
        }

        .claude-menu .ant-menu-item:hover {
          background: var(--dark-surface) !important;
          color: var(--ivory);
        }

        .claude-menu .ant-menu-item-selected {
          background: var(--dark-surface) !important;
          color: var(--ivory) !important;
        }

        .claude-menu .ant-menu-item-selected::after {
          display: none;
        }

        .claude-menu .ant-menu-item .anticon {
          font-size: 18px;
        }

        .claude-menu .ant-menu-item-selected .anticon {
          color: var(--terracotta-brand);
        }

        @media (max-width: 768px) {
          .claude-sidebar {
            display: none;
          }
        }
      `})]})}const{Content:kt}=_;function Rt(){const[e,t]=m.useState(!1);return o.jsxs(_,{style:{minHeight:"100vh"},children:[o.jsx(Ot,{collapsed:e}),o.jsxs(_,{children:[o.jsx($t,{collapsed:e,onToggle:()=>t(!e)}),o.jsx(kt,{style:{margin:16},children:o.jsx(de,{})})]})]})}const Ct=m.lazy(()=>y(()=>import("./index-BbLZ5P7D.js"),__vite__mapDeps([0,1,2,3]))),Dt=m.lazy(()=>y(()=>import("./index-DYoibeTn.js"),__vite__mapDeps([4,1,5,2,3]))),It=m.lazy(()=>y(()=>import("./index-DWkQvzl-.js"),__vite__mapDeps([6,1,5,2,3]))),Nt=m.lazy(()=>y(()=>import("./index-BXuHDRNt.js"),__vite__mapDeps([7,1,8,3,9,5,2]))),zt=m.lazy(()=>y(()=>import("./index-Doz38uUw.js"),__vite__mapDeps([10,1,8,3,2,5]))),Tt=m.lazy(()=>y(()=>import("./index-Bml7pWNB.js"),__vite__mapDeps([11,1,3,9,2,12]))),Lt=m.lazy(()=>y(()=>import("./index-D8tAifjj.js"),__vite__mapDeps([13,1,3,2,12]))),At=m.lazy(()=>y(()=>import("./index-2Bm9Uafs.js"),__vite__mapDeps([14,1,2,3])));function Mt({children:e}){const{token:t}=z();return t?o.jsx(o.Fragment,{children:e}):o.jsx(B,{to:"/login",replace:!0})}function Vt(){return o.jsx("div",{style:{display:"flex",justifyContent:"center",marginTop:100},children:o.jsx(je,{size:"large"})})}function Ft(){return o.jsx(m.Suspense,{fallback:o.jsx(Vt,{}),children:o.jsxs(pe,{children:[o.jsx(x,{path:"/login",element:o.jsx(Ct,{})}),o.jsxs(x,{path:"/",element:o.jsx(Mt,{children:o.jsx(Rt,{})}),children:[o.jsx(x,{index:!0,element:o.jsx(B,{to:"/dashboard",replace:!0})}),o.jsx(x,{path:"dashboard",element:o.jsx(Dt,{})}),o.jsx(x,{path:"patient-search",element:o.jsx(It,{})}),o.jsx(x,{path:"patient360/:patientId",element:o.jsx(Nt,{})}),o.jsx(x,{path:"timeline/:patientId",element:o.jsx(zt,{})}),o.jsx(x,{path:"lab/:orderId",element:o.jsx(Tt,{})}),o.jsx(x,{path:"imaging/:orderId",element:o.jsx(Lt,{})}),o.jsx(x,{path:"system",element:o.jsx(At,{})})]})]})})}N.createRoot(document.getElementById("root")).render(o.jsx(q.StrictMode,{children:o.jsx(Se,{locale:He,children:o.jsx(fe,{children:o.jsx(Ft,{})})})}));export{o as j,z as u};
