import{j as a}from"./index-BWQwxX9G.js";function s({summary:i,loading:r}){return r?a.jsxs("div",{className:"ai-summary-card loading",children:[a.jsxs("div",{className:"ai-summary-header",children:[a.jsx("div",{className:"ai-icon",children:a.jsx("svg",{width:"24",height:"24",viewBox:"0 0 24 24",fill:"none",xmlns:"http://www.w3.org/2000/svg",children:a.jsx("path",{d:"M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z",fill:"currentColor"})})}),a.jsx("span",{className:"ai-title",children:"AI 辅助摘要"}),a.jsx("span",{className:"ai-badge",children:"生成中..."})]}),a.jsxs("div",{className:"ai-summary-loading",children:[a.jsx("div",{className:"loading-bar"}),a.jsx("div",{className:"loading-bar"}),a.jsx("div",{className:"loading-bar short"})]})]}):a.jsxs("div",{className:"ai-summary-card",children:[a.jsxs("div",{className:"ai-summary-header",children:[a.jsx("div",{className:"ai-icon",children:a.jsx("svg",{width:"24",height:"24",viewBox:"0 0 24 24",fill:"none",xmlns:"http://www.w3.org/2000/svg",children:a.jsx("path",{d:"M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z",fill:"currentColor"})})}),a.jsx("span",{className:"ai-title",children:"AI 辅助摘要"}),a.jsx("span",{className:"ai-badge",children:"参考"})]}),a.jsx("div",{className:"ai-summary-content",children:i||"暂无摘要内容"}),a.jsx("div",{className:"ai-summary-footer",children:a.jsx("span",{className:"ai-disclaimer",children:"辅助生成，仅供参考"})}),a.jsx("style",{children:`
        .ai-summary-card {
          background: var(--ivory);
          border: 1px solid var(--border-cream);
          border-radius: 16px;
          padding: 24px 28px;
          margin-top: 32px;
          position: relative;
          overflow: hidden;
        }

        .ai-summary-card::before {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          width: 4px;
          height: 100%;
          background: linear-gradient(180deg, var(--terracotta-brand) 0%, var(--coral-accent) 100%);
          border-radius: 4px 0 0 4px;
        }

        .ai-summary-header {
          display: flex;
          align-items: center;
          gap: 12px;
          margin-bottom: 20px;
        }

        .ai-icon {
          width: 40px;
          height: 40px;
          background: linear-gradient(135deg, var(--terracotta-brand) 0%, var(--coral-accent) 100%);
          border-radius: 10px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
        }

        .ai-title {
          font-family: Georgia, serif;
          font-size: 20px;
          font-weight: 500;
          color: var(--anthropic-near-black);
        }

        .ai-badge {
          margin-left: auto;
          padding: 4px 12px;
          font-size: 12px;
          font-weight: 600;
          color: var(--olive-gray);
          background: var(--warm-sand);
          border-radius: 20px;
          text-transform: uppercase;
          letter-spacing: 0.5px;
        }

        .ai-summary-content {
          font-size: 16px;
          line-height: 1.80;
          color: var(--charcoal-warm);
          padding: 20px 24px;
          background: var(--warm-sand);
          border-radius: 12px;
          margin-bottom: 16px;
        }

        .ai-summary-footer {
          display: flex;
          justify-content: flex-end;
        }

        .ai-disclaimer {
          font-size: 13px;
          color: var(--stone-gray);
          font-style: italic;
        }

        /* Loading State */
        .ai-summary-card.loading .ai-badge {
          color: var(--terracotta-brand);
          background: rgba(201, 100, 66, 0.1);
        }

        .ai-summary-loading {
          display: flex;
          flex-direction: column;
          gap: 12px;
        }

        .loading-bar {
          height: 16px;
          background: var(--warm-sand);
          border-radius: 8px;
          animation: pulse 1.5s ease-in-out infinite;
        }

        .loading-bar:nth-child(1) { width: 100%; }
        .loading-bar:nth-child(2) { width: 85%; }
        .loading-bar:nth-child(3) { width: 60%; }

        .loading-bar.short {
          width: 40%;
        }

        @keyframes pulse {
          0%, 100% { opacity: 1; }
          50% { opacity: 0.5; }
        }
      `})]})}export{s as A};
