import{j as e}from"./index-BWQwxX9G.js";import{T as n}from"./antd-C0pnNR-A.js";function c({patient:a,encounter:i}){const r=a.birthDate?new Date().getFullYear()-new Date(a.birthDate).getFullYear():"-",t={门诊:"#3898ec",住院:"#3d8c40",急诊:"#b53333"}[i.encounterType]||"#87867f";return e.jsxs("div",{className:"patient-header",children:[e.jsxs("div",{className:"patient-header-left",children:[e.jsx("div",{className:"patient-avatar-large",children:a.name.charAt(0)}),e.jsxs("div",{className:"patient-header-info",children:[e.jsx("h1",{className:"patient-name",children:a.name}),e.jsxs("div",{className:"patient-meta-row",children:[e.jsx("span",{className:"meta-item",children:a.gender}),e.jsx("span",{className:"meta-divider",children:"·"}),e.jsxs("span",{className:"meta-item",children:[r,"岁"]}),e.jsx("span",{className:"meta-divider",children:"·"}),e.jsxs("span",{className:"meta-item",children:["就诊号: ",a.unifiedPatientId]})]})]})]}),e.jsxs("div",{className:"patient-header-right",children:[e.jsx("div",{className:"encounter-badge",style:{borderColor:t},children:e.jsx(n,{color:t,style:{margin:0,border:"none"},children:i.encounterType})}),e.jsxs("div",{className:"encounter-details",children:[e.jsxs("div",{className:"encounter-detail",children:[e.jsx("span",{className:"detail-label",children:"科室"}),e.jsx("span",{className:"detail-value",children:i.departmentName})]}),e.jsxs("div",{className:"encounter-detail",children:[e.jsx("span",{className:"detail-label",children:"就诊时间"}),e.jsx("span",{className:"detail-value",children:new Date(i.visitDatetime).toLocaleDateString("zh-CN",{year:"numeric",month:"long",day:"numeric"})})]}),e.jsxs("div",{className:"encounter-detail full-width",children:[e.jsx("span",{className:"detail-label",children:"就诊原因"}),e.jsx("span",{className:"detail-value",children:i.admissionReason})]})]})]}),e.jsx("style",{children:`
        .patient-header {
          display: flex;
          justify-content: space-between;
          align-items: flex-start;
          background: var(--ivory);
          border: 1px solid var(--border-cream);
          border-radius: 20px;
          padding: 32px;
          box-shadow: 0px 0px 0px 1px var(--border-cream);
        }

        .patient-header-left {
          display: flex;
          align-items: center;
          gap: 24px;
        }

        .patient-avatar-large {
          width: 72px;
          height: 72px;
          background: var(--anthropic-near-black);
          color: var(--ivory);
          border-radius: 16px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-family: Georgia, serif;
          font-size: 32px;
          font-weight: 500;
        }

        .patient-name {
          font-family: Georgia, 'Times New Roman', serif;
          font-size: 36px;
          font-weight: 500;
          color: var(--anthropic-near-black);
          margin: 0 0 8px;
          line-height: 1.2;
        }

        .patient-meta-row {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 15px;
          color: var(--olive-gray);
        }

        .meta-divider {
          color: var(--stone-gray);
        }

        .patient-header-right {
          display: flex;
          flex-direction: column;
          align-items: flex-end;
          gap: 16px;
        }

        .encounter-badge {
          background: var(--warm-sand);
          border: 2px solid;
          border-radius: 8px;
          padding: 4px 12px;
        }

        .encounter-details {
          display: grid;
          grid-template-columns: repeat(2, auto);
          gap: 12px 32px;
          text-align: right;
        }

        .encounter-detail {
          display: flex;
          flex-direction: column;
          gap: 2px;
        }

        .encounter-detail.full-width {
          grid-column: span 2;
          text-align: right;
        }

        .detail-label {
          font-size: 12px;
          font-weight: 600;
          color: var(--stone-gray);
          text-transform: uppercase;
          letter-spacing: 0.5px;
        }

        .detail-value {
          font-size: 15px;
          color: var(--charcoal-warm);
          font-weight: 500;
        }

        @media (max-width: 768px) {
          .patient-header {
            flex-direction: column;
            gap: 24px;
          }
          .patient-header-right {
            align-items: flex-start;
            width: 100%;
          }
          .encounter-details {
            grid-template-columns: 1fr 1fr;
          }
          .encounter-detail.full-width {
            grid-column: span 2;
          }
        }
      `})]})}export{c as P};
