import{j as a}from"./index-BWQwxX9G.js";import{j as u,u as j,r as i}from"./vendor-CSj9hcp_.js";import{P as N}from"./PatientHeader-CN4lP8q0.js";import{A as y}from"./AISummaryCard-aPTQjga6.js";import{a as w}from"./patient-mx5b0GGp.js";import{S as k,j as m,k as p,l as z,m as S,n as C}from"./antd-C0pnNR-A.js";import"./index-Bd8g9rLw.js";function Y(){const{patientId:s}=u(),t=j(),[n,x]=i.useState(null),[b,c]=i.useState(!0);i.useEffect(()=>{s&&h()},[s]);const h=async()=>{c(!0);try{const e=await w(Number(s));x(e.data)}catch(e){console.error("加载患者360数据失败",e)}finally{c(!1)}};if(b)return a.jsxs("div",{className:"loading-container",children:[a.jsx(k,{size:"large"}),a.jsx("p",{className:"loading-text",children:"正在加载患者数据..."})]});if(!n)return a.jsx("div",{className:"error-container",children:"未找到患者数据"});const{patient:v,encounter:g,labSummary:r,imagingSummary:o,aiSummary:f}=n;return a.jsxs("div",{className:"patient360-container",children:[a.jsx(N,{patient:v,encounter:g}),a.jsx("section",{className:"patient-stats-section",children:a.jsxs("div",{className:"stats-grid-3",children:[a.jsxs("div",{className:"patient-stat-card",children:[a.jsx("div",{className:"stat-icon-wrapper",style:{background:"rgba(56, 152, 236, 0.1)"},children:a.jsx(m,{style:{color:"#3898ec",fontSize:24}})}),a.jsxs("div",{className:"stat-content",children:[a.jsx("div",{className:"stat-number",children:r.recentCount}),a.jsx("div",{className:"stat-title",children:"近期检验"})]})]}),a.jsxs("div",{className:"patient-stat-card",children:[a.jsx("div",{className:"stat-icon-wrapper",style:{background:"rgba(201, 100, 66, 0.1)"},children:a.jsx(p,{style:{color:"#c96442",fontSize:24}})}),a.jsxs("div",{className:"stat-content",children:[a.jsx("div",{className:"stat-number",children:o.recentCount}),a.jsx("div",{className:"stat-title",children:"近期影像"})]})]}),a.jsxs("div",{className:"patient-stat-card alert",children:[a.jsx("div",{className:"stat-icon-wrapper",style:{background:"rgba(181, 51, 51, 0.1)"},children:a.jsx(z,{style:{color:"#b53333",fontSize:24}})}),a.jsxs("div",{className:"stat-content",children:[a.jsx("div",{className:"stat-number",style:{color:"#b53333"},children:r.abnormalCount}),a.jsx("div",{className:"stat-title",children:"异常项"})]})]})]})}),a.jsx("section",{className:"patient-nav-section",children:a.jsxs("div",{className:"nav-card",onClick:()=>t(`/timeline/${s}`),children:[a.jsx("div",{className:"nav-icon",children:a.jsx(S,{})}),a.jsxs("div",{className:"nav-content",children:[a.jsx("div",{className:"nav-title",children:"患者时间轴"}),a.jsx("div",{className:"nav-desc",children:"查看完整就诊事件序列"})]}),a.jsx("div",{className:"nav-arrow",children:a.jsx(C,{})})]})}),a.jsxs("section",{className:"patient-data-section",children:[a.jsxs("div",{className:"section-header",children:[a.jsxs("h2",{className:"section-title",children:[a.jsx(m,{style:{marginRight:12,color:"#3898ec"}}),"检验记录"]}),a.jsx("button",{className:"view-all-button",onClick:()=>t("/patient-search"),children:"查看全部"})]}),a.jsxs("div",{className:"data-table",children:[a.jsxs("div",{className:"table-header",children:[a.jsx("div",{className:"table-cell",children:"检验单号"}),a.jsx("div",{className:"table-cell",children:"时间"}),a.jsx("div",{className:"table-cell",children:"标本类型"}),a.jsx("div",{className:"table-cell",children:"科室"}),a.jsx("div",{className:"table-cell",children:"状态"}),a.jsx("div",{className:"table-cell",children:"操作"})]}),r.recentOrders.map((e,l)=>{var d;return a.jsxs("div",{className:"table-row",style:{animationDelay:`${l*50}ms`},children:[a.jsx("div",{className:"table-cell","data-label":"单号",children:e.orderNo}),a.jsx("div",{className:"table-cell","data-label":"时间",children:new Date(e.orderDatetime).toLocaleDateString("zh-CN")}),a.jsx("div",{className:"table-cell","data-label":"标本",children:e.specimenType}),a.jsx("div",{className:"table-cell","data-label":"科室",children:e.departmentName}),a.jsx("div",{className:"table-cell","data-label":"状态",children:a.jsx("span",{className:`status-badge status-${(d=e.status)==null?void 0:d.toLowerCase()}`,children:e.status})}),a.jsx("div",{className:"table-cell","data-label":"操作",children:a.jsx("button",{className:"action-link",onClick:()=>t(`/lab/${e.id}`),children:"查看详情"})})]},e.id)})]})]}),a.jsxs("section",{className:"patient-data-section",children:[a.jsxs("div",{className:"section-header",children:[a.jsxs("h2",{className:"section-title",children:[a.jsx(p,{style:{marginRight:12,color:"#c96442"}}),"影像记录"]}),a.jsx("button",{className:"view-all-button",onClick:()=>t("/patient-search"),children:"查看全部"})]}),a.jsxs("div",{className:"data-table",children:[a.jsxs("div",{className:"table-header",children:[a.jsx("div",{className:"table-cell",children:"检查单号"}),a.jsx("div",{className:"table-cell",children:"时间"}),a.jsx("div",{className:"table-cell",children:"检查部位"}),a.jsx("div",{className:"table-cell",children:"检查方式"}),a.jsx("div",{className:"table-cell",children:"科室"}),a.jsx("div",{className:"table-cell",children:"操作"})]}),o.recentOrders.map((e,l)=>a.jsxs("div",{className:"table-row",style:{animationDelay:`${l*50}ms`},children:[a.jsx("div",{className:"table-cell","data-label":"单号",children:e.orderNo}),a.jsx("div",{className:"table-cell","data-label":"时间",children:new Date(e.orderDatetime).toLocaleDateString("zh-CN")}),a.jsx("div",{className:"table-cell","data-label":"部位",children:e.bodyPart}),a.jsx("div",{className:"table-cell","data-label":"方式",children:e.modality}),a.jsx("div",{className:"table-cell","data-label":"科室",children:e.departmentName}),a.jsx("div",{className:"table-cell","data-label":"操作",children:a.jsx("button",{className:"action-link",onClick:()=>t(`/imaging/${e.id}`),children:"查看报告"})})]},e.id))]})]}),a.jsx(y,{summary:f}),a.jsx("style",{children:`
        .patient360-container {
          min-height: 100vh;
          background: var(--parchment);
          padding: 32px 48px 48px;
        }

        .loading-container {
          min-height: 100vh;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          gap: 16px;
        }

        .loading-text {
          color: var(--olive-gray);
          font-size: 16px;
        }

        .error-container {
          min-height: 100vh;
          display: flex;
          align-items: center;
          justify-content: center;
          color: var(--error-crimson);
          font-size: 18px;
        }

        /* Stats Section */
        .patient-stats-section {
          margin: 32px 0;
        }

        .stats-grid-3 {
          display: grid;
          grid-template-columns: repeat(3, 1fr);
          gap: 20px;
        }

        .patient-stat-card {
          display: flex;
          align-items: center;
          gap: 20px;
          background: var(--ivory);
          border: 1px solid var(--border-cream);
          border-radius: 16px;
          padding: 24px 28px;
          transition: all 0.2s ease;
        }

        .patient-stat-card:hover {
          transform: translateY(-2px);
          box-shadow: rgba(0, 0, 0, 0.05) 0px 4px 24px;
        }

        .stat-icon-wrapper {
          width: 56px;
          height: 56px;
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
        }

        .stat-number {
          font-family: Georgia, serif;
          font-size: 36px;
          font-weight: 500;
          color: var(--anthropic-near-black);
          line-height: 1;
          margin-bottom: 4px;
        }

        .stat-title {
          font-size: 15px;
          color: var(--olive-gray);
          font-weight: 500;
        }

        /* Navigation Section */
        .patient-nav-section {
          margin-bottom: 40px;
        }

        .nav-card {
          display: flex;
          align-items: center;
          gap: 20px;
          background: var(--anthropic-near-black);
          border-radius: 16px;
          padding: 24px 32px;
          cursor: pointer;
          transition: all 0.2s ease;
        }

        .nav-card:hover {
          background: var(--dark-surface);
          transform: translateX(4px);
        }

        .nav-icon {
          width: 48px;
          height: 48px;
          background: var(--terracotta-brand);
          border-radius: 10px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 20px;
          color: var(--ivory);
        }

        .nav-title {
          font-size: 18px;
          font-weight: 500;
          color: var(--ivory);
          margin-bottom: 4px;
        }

        .nav-desc {
          font-size: 14px;
          color: var(--warm-silver);
        }

        .nav-arrow {
          margin-left: auto;
          color: var(--warm-silver);
          font-size: 16px;
        }

        /* Data Section */
        .patient-data-section {
          margin-bottom: 40px;
        }

        .section-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;
        }

        .section-title {
          font-family: Georgia, serif;
          font-size: 25px;
          font-weight: 500;
          color: var(--anthropic-near-black);
          margin: 0;
        }

        .view-all-button {
          padding: 8px 16px;
          font-size: 14px;
          font-weight: 500;
          color: var(--terracotta-brand);
          background: transparent;
          border: 1px solid var(--terracotta-brand);
          border-radius: 8px;
          cursor: pointer;
          transition: all 0.2s ease;
        }

        .view-all-button:hover {
          background: var(--terracotta-brand);
          color: var(--ivory);
        }

        /* Data Table */
        .data-table {
          background: var(--ivory);
          border: 1px solid var(--border-cream);
          border-radius: 16px;
          overflow: hidden;
        }

        .table-header {
          display: grid;
          grid-template-columns: 1.5fr 1fr 1fr 1fr 1fr 1fr;
          padding: 16px 24px;
          background: var(--warm-sand);
          border-bottom: 1px solid var(--border-warm);
        }

        .table-row {
          display: grid;
          grid-template-columns: 1.5fr 1fr 1fr 1fr 1fr 1fr;
          padding: 18px 24px;
          border-bottom: 1px solid var(--border-cream);
          transition: background 0.15s ease;
          animation: fadeInUp 0.3s ease forwards;
          opacity: 0;
        }

        @keyframes fadeInUp {
          from {
            opacity: 0;
            transform: translateY(8px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }

        .table-row:last-child {
          border-bottom: none;
        }

        .table-row:hover {
          background: var(--warm-sand);
        }

        .table-cell {
          font-size: 15px;
          color: var(--charcoal-warm);
          display: flex;
          align-items: center;
        }

        .table-header .table-cell {
          font-size: 13px;
          font-weight: 600;
          color: var(--olive-gray);
          text-transform: uppercase;
          letter-spacing: 0.5px;
        }

        .status-badge {
          display: inline-block;
          padding: 4px 12px;
          font-size: 12px;
          font-weight: 600;
          border-radius: 20px;
          text-transform: uppercase;
        }

        .status-badge.status-completed,
        .status-badge.status-reported {
          background: rgba(61, 140, 64, 0.1);
          color: #3d8c40;
        }

        .status-badge.status-pending,
        .status-badge.status-processing {
          background: rgba(201, 100, 66, 0.1);
          color: #c96442;
        }

        .action-link {
          background: none;
          border: none;
          color: var(--terracotta-brand);
          font-size: 14px;
          font-weight: 500;
          cursor: pointer;
          padding: 0;
          transition: color 0.15s ease;
        }

        .action-link:hover {
          color: #b85a3a;
          text-decoration: underline;
        }

        @media (max-width: 992px) {
          .stats-grid-3 {
            grid-template-columns: 1fr;
          }
          .table-header, .table-row {
            grid-template-columns: 1fr 1fr 1fr;
          }
          .table-cell:nth-child(4),
          .table-cell:nth-child(5) {
            display: none;
          }
        }

        @media (max-width: 640px) {
          .patient360-container {
            padding: 24px;
          }
          .table-header {
            display: none;
          }
          .table-row {
            display: flex;
            flex-direction: column;
            gap: 8px;
            padding: 16px;
          }
          .table-cell {
            display: flex;
          }
          .table-cell::before {
            content: attr(data-label);
            width: 80px;
            font-size: 12px;
            font-weight: 600;
            color: var(--stone-gray);
            text-transform: uppercase;
          }
        }
      `})]})}export{Y as default};
