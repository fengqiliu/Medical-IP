import{j as a}from"./index-BWQwxX9G.js";import{u as v,r as i}from"./vendor-CSj9hcp_.js";import{g as f,s as j}from"./patient-mx5b0GGp.js";import{g as k,h as m,S as N}from"./antd-C0pnNR-A.js";import"./index-Bd8g9rLw.js";function n({label:r,value:s,accent:t="blue"}){const c={green:"var(--success-green, #3d8c40)",blue:"var(--focus-blue, #3898ec)",orange:"var(--terracotta-brand, #c96442)",crimson:"var(--error-crimson, #b53333)"};return a.jsxs("div",{className:"stat-card",style:{"--accent-color":c[t]},children:[a.jsxs("div",{className:"stat-card-inner",children:[a.jsx("div",{className:"stat-value",style:{color:c[t]},children:s}),a.jsx("div",{className:"stat-label",children:r})]}),a.jsx("div",{className:"stat-indicator",style:{background:c[t]}})]})}function C(){const r=v(),[s,t]=i.useState(null),[c,u]=i.useState(!0),[o,g]=i.useState(""),[l,d]=i.useState([]),[p,x]=i.useState(!1);i.useEffect(()=>{f().then(e=>t(e.data)).catch(()=>t(null)).finally(()=>u(!1))},[]);const h=async e=>{if(!e.trim()){d([]);return}x(!0);try{const b=await j(e.trim());d(b.data||[])}catch{d([])}finally{x(!1)}};return a.jsxs("div",{className:"dashboard-container",children:[a.jsxs("header",{className:"dashboard-header",children:[a.jsxs("div",{className:"dashboard-header-content",children:[a.jsx("h1",{className:"dashboard-title",children:"工作台"}),a.jsx("p",{className:"dashboard-subtitle",children:"临床医技360全景系统 · 实时数据概览"})]}),a.jsxs("div",{className:"dashboard-date",children:[a.jsx("span",{className:"date-day",children:"24"}),a.jsx("span",{className:"date-month",children:"四月"})]})]}),a.jsxs("section",{className:"search-section",children:[a.jsxs("div",{className:"search-wrapper",children:[a.jsx("div",{className:"search-icon",children:a.jsx(k,{})}),a.jsx("input",{type:"text",className:"search-input",placeholder:"输入患者姓名 / 就诊号 / 身份证号搜索...",value:o,onChange:e=>g(e.target.value),onKeyDown:e=>e.key==="Enter"&&h(o)}),a.jsx("button",{className:"search-button",onClick:()=>h(o),disabled:p,children:p?"搜索中...":"搜索"})]}),l.length>0&&a.jsx("div",{className:"search-results",children:a.jsx(m,{dataSource:l,renderItem:e=>a.jsxs(m.Item,{className:"search-result-item",onClick:()=>r(`/patient360/${e.id}`),children:[a.jsx("div",{className:"patient-avatar",children:e.name.charAt(0)}),a.jsxs("div",{className:"patient-info",children:[a.jsx("div",{className:"patient-name",children:e.name}),a.jsxs("div",{className:"patient-meta",children:["就诊号: ",e.unifiedPatientId," · ",e.gender," ·"," ",e.idCardNo||"未登记"]})]}),a.jsx("div",{className:"patient-action",children:a.jsx("span",{children:"查看详情 →"})})]})})})]}),a.jsx("section",{className:"stats-section",children:a.jsx(N,{spinning:c,children:a.jsxs("div",{className:"stats-grid",children:[a.jsx(n,{label:"今日检验",value:(s==null?void 0:s.todayLab)??"—",accent:"blue"}),a.jsx(n,{label:"今日影像",value:(s==null?void 0:s.todayImaging)??"—",accent:"orange"}),a.jsx(n,{label:"新增患者",value:(s==null?void 0:s.todayNewPatient)??"—",accent:"green"}),a.jsx(n,{label:"异常提醒",value:(s==null?void 0:s.abnormalAlert)??"—",accent:"crimson"})]})})}),a.jsxs("section",{className:"quick-access-section",children:[a.jsx("h2",{className:"section-title",children:"快速访问"}),a.jsxs("div",{className:"quick-access-grid",children:[a.jsxs("div",{className:"quick-access-card",onClick:()=>r("/patient-search"),children:[a.jsx("div",{className:"quick-access-icon",children:"🔍"}),a.jsx("div",{className:"quick-access-title",children:"患者搜索"}),a.jsx("div",{className:"quick-access-desc",children:"通过多条件检索患者信息"})]}),a.jsxs("div",{className:"quick-access-card",onClick:()=>r("/timeline/1"),children:[a.jsx("div",{className:"quick-access-icon",children:"📅"}),a.jsx("div",{className:"quick-access-title",children:"时间线"}),a.jsx("div",{className:"quick-access-desc",children:"查看患者就诊事件序列"})]}),a.jsxs("div",{className:"quick-access-card",onClick:()=>r("/lab/1"),children:[a.jsx("div",{className:"quick-access-icon",children:"🧪"}),a.jsx("div",{className:"quick-access-title",children:"检验报告"}),a.jsx("div",{className:"quick-access-desc",children:"查看检验详细结果"})]}),a.jsxs("div",{className:"quick-access-card",onClick:()=>r("/imaging/1"),children:[a.jsx("div",{className:"quick-access-icon",children:"📷"}),a.jsx("div",{className:"quick-access-title",children:"影像报告"}),a.jsx("div",{className:"quick-access-desc",children:"查看影像检查报告"})]})]})]}),a.jsx("style",{children:`
        .dashboard-container {
          min-height: 100vh;
          background: var(--parchment);
          padding: 0 48px 48px;
        }

        /* Header */
        .dashboard-header {
          display: flex;
          justify-content: space-between;
          align-items: flex-start;
          padding: 48px 0 40px;
          border-bottom: 1px solid var(--border-cream);
          margin-bottom: 40px;
        }

        .dashboard-title {
          font-family: Georgia, 'Times New Roman', serif;
          font-size: 52px;
          font-weight: 500;
          line-height: 1.20;
          color: var(--anthropic-near-black);
          margin: 0 0 8px;
        }

        .dashboard-subtitle {
          font-size: 17px;
          color: var(--olive-gray);
          line-height: 1.60;
          margin: 0;
        }

        .dashboard-date {
          text-align: right;
          padding: 8px 16px;
          background: var(--ivory);
          border-radius: 12px;
          border: 1px solid var(--border-cream);
        }

        .date-day {
          display: block;
          font-family: Georgia, serif;
          font-size: 36px;
          font-weight: 500;
          color: var(--terracotta-brand);
          line-height: 1;
        }

        .date-month {
          font-size: 14px;
          color: var(--stone-gray);
        }

        /* Search Section */
        .search-section {
          margin-bottom: 48px;
        }

        .search-wrapper {
          display: flex;
          align-items: center;
          background: var(--ivory);
          border: 1px solid var(--border-warm);
          border-radius: 16px;
          padding: 8px 8px 8px 20px;
          box-shadow: 0px 0px 0px 1px var(--border-cream);
          transition: all 0.2s ease;
        }

        .search-wrapper:focus-within {
          border-color: var(--focus-blue);
          box-shadow: 0px 0px 0px 3px rgba(56, 152, 236, 0.15);
        }

        .search-icon {
          color: var(--stone-gray);
          font-size: 20px;
          margin-right: 12px;
        }

        .search-input {
          flex: 1;
          border: none;
          background: transparent;
          font-size: 17px;
          color: var(--anthropic-near-black);
          outline: none;
          padding: 12px 0;
        }

        .search-input::placeholder {
          color: var(--stone-gray);
        }

        .search-button {
          padding: 14px 28px;
          font-size: 16px;
          font-weight: 500;
          color: var(--ivory);
          background: var(--anthropic-near-black);
          border: none;
          border-radius: 10px;
          cursor: pointer;
          transition: all 0.2s ease;
        }

        .search-button:hover {
          background: var(--dark-surface);
        }

        .search-button:disabled {
          opacity: 0.6;
          cursor: not-allowed;
        }

        /* Search Results */
        .search-results {
          margin-top: 16px;
          background: var(--pure-white);
          border: 1px solid var(--border-cream);
          border-radius: 12px;
          overflow: hidden;
        }

        .search-result-item {
          display: flex;
          align-items: center;
          padding: 16px 20px;
          cursor: pointer;
          transition: background 0.15s ease;
          border-bottom: 1px solid var(--border-cream);
        }

        .search-result-item:last-child {
          border-bottom: none;
        }

        .search-result-item:hover {
          background: var(--warm-sand);
        }

        .patient-avatar {
          width: 44px;
          height: 44px;
          background: var(--anthropic-near-black);
          color: var(--ivory);
          border-radius: 10px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-family: Georgia, serif;
          font-size: 18px;
          font-weight: 500;
          margin-right: 16px;
        }

        .patient-info {
          flex: 1;
        }

        .patient-name {
          font-size: 16px;
          font-weight: 500;
          color: var(--anthropic-near-black);
          margin-bottom: 4px;
        }

        .patient-meta {
          font-size: 14px;
          color: var(--stone-gray);
        }

        .patient-action {
          font-size: 14px;
          color: var(--terracotta-brand);
          font-weight: 500;
        }

        /* Stats Section */
        .stats-section {
          margin-bottom: 48px;
        }

        .stats-grid {
          display: grid;
          grid-template-columns: repeat(4, 1fr);
          gap: 20px;
        }

        .stat-card {
          background: var(--ivory);
          border-radius: 16px;
          padding: 28px;
          position: relative;
          overflow: hidden;
          box-shadow: 0px 0px 0px 1px var(--border-cream);
          transition: all 0.2s ease;
        }

        .stat-card:hover {
          transform: translateY(-2px);
          box-shadow: 0px 0px 0px 1px var(--border-cream), rgba(0, 0, 0, 0.05) 0px 8px 24px;
        }

        .stat-card-inner {
          position: relative;
          z-index: 1;
        }

        .stat-value {
          font-family: Georgia, serif;
          font-size: 48px;
          font-weight: 500;
          line-height: 1;
          margin-bottom: 8px;
        }

        .stat-label {
          font-size: 15px;
          color: var(--olive-gray);
          font-weight: 500;
        }

        .stat-indicator {
          position: absolute;
          bottom: 0;
          left: 0;
          right: 0;
          height: 4px;
        }

        /* Quick Access Section */
        .quick-access-section {
          margin-bottom: 48px;
        }

        .section-title {
          font-family: Georgia, serif;
          font-size: 25px;
          font-weight: 500;
          color: var(--anthropic-near-black);
          margin-bottom: 24px;
        }

        .quick-access-grid {
          display: grid;
          grid-template-columns: repeat(4, 1fr);
          gap: 20px;
        }

        .quick-access-card {
          background: var(--ivory);
          border: 1px solid var(--border-warm);
          border-radius: 16px;
          padding: 28px;
          cursor: pointer;
          transition: all 0.2s ease;
        }

        .quick-access-card:hover {
          border-color: var(--terracotta-brand);
          transform: translateY(-2px);
          box-shadow: 0px 0px 0px 1px var(--terracotta-brand), rgba(0, 0, 0, 0.05) 0px 8px 24px;
        }

        .quick-access-icon {
          font-size: 32px;
          margin-bottom: 16px;
        }

        .quick-access-title {
          font-size: 17px;
          font-weight: 500;
          color: var(--anthropic-near-black);
          margin-bottom: 8px;
        }

        .quick-access-desc {
          font-size: 14px;
          color: var(--stone-gray);
          line-height: 1.50;
        }

        @media (max-width: 992px) {
          .stats-grid, .quick-access-grid {
            grid-template-columns: repeat(2, 1fr);
          }
        }

        @media (max-width: 640px) {
          .dashboard-container {
            padding: 0 24px 24px;
          }
          .stats-grid, .quick-access-grid {
            grid-template-columns: 1fr;
          }
          .dashboard-title {
            font-size: 36px;
          }
        }
      `})]})}export{C as default};
