# 芳沈手册（语法糖手册）

一款面向精神医学用药自我管理的原生 Android 应用：记录服药剂量，把每种药的血药浓度与效应变化绘制成示意图，并在联合用药时给出相互作用与负荷预警。

> 本软件为科普辅助工具，所有浓度曲线与效应推演均为**示意性估算**，不构成任何医疗建议。用药请遵医嘱。

## 功能一览

- **服药记录**：按药库点选服药（单片剂量自动带出），也可手动输入任意剂量；自定义药物与剂量随手补录。
- **血药浓度示意图**：以 Bateman 吸收-消除模型逐点绘制当前各药的浓度曲线（主页，横轴以 12 小时为单位），点按图表可切换 毫克 / 克 / 微克 单位。
- **效应引擎**：把剂量规则映射为 30 余种主观/客观效应（镇静、欣快、镇痛……），含反向抵消（如兴奋抵消镇静）与权重合并，形成当前效应面板。
- **联用预警**：
  - **酶学层**：CYP 等代谢酶的抑制/诱导/竞争，按分子标签推断浓度变化方向；
  - **药效学层**：机制配对（MAOI × 5-HT 能 → 5-HT 综合征；阿片 × GABA → 协同性呼吸抑制；NSAID × 锂盐 → 锂蓄积 等）双向检查；
  - **负荷层**：血清素能 ≥2、抗胆碱能 ≥2、中枢抑制 ≥3 时提示叠加负荷。
- **自定义《预警》**：按“药品组合 / 个人画像（11 类 DSM-5 障碍、73 项条目）→ 附加效应权重”配置规则，可逐侧开关“按药物 / 按标签”匹配。
- **处方模块**：JSON 处方文件记录药物组合与适应症，进入编辑页即实时估算联用效应与相互作用，并支持长按截图分享。
- **药库**：内置 110 种有精神效应的物质，覆盖 SSRI/SNRI、三环/四环、MAOI、三代抗精神病药、心境稳定剂、苯二氮卓类、兴奋剂、镇痛药（NSAIDs、阿片类、奈福泮）、胆碱酯酶抑制剂、保健品与食品、尼古丁/乙醇/槟榔碱/咖啡因。

## 技术栈

- Kotlin + Jetpack Compose，Material 3，浅色竖屏手机形态
- 图表：[Vico](https://github.com/patrykandpatrick/vico) 2.x
- 剂量以 `BigDecimal` 存储与运算，避免浮点误差
- 数据为设备本地 JSON（应用专有目录），无云端、无账号

## 目录结构

```
app/src/main/java/com/fangshen/manual/
├── model/          DrugCatalog(110 种内置药)、Effect、Prescription、TagColors…
├── engine/         PkEngine(药代动力学)、EffectEngine(效应)、InteractionEngine(联用预警)、AlertRules
├── ui/home/        主页(浓度曲线+效应面板)、服药抽屉、左右抽屉
├── ui/prescription/处方列表/编辑/分析
├── ui/dose/        服药明细录入
└── ui/theme/       Material 3 主题
```

## 构建与安装

需要 JDK 17、Android SDK（compileSdk 35）、Gradle 8.10.2：

```bash
git clone git@github.com:inline-function/Function-Manual.git
cd Function-Manual
gradle --no-daemon assembleRelease
# 产物：app/build/outputs/apk/release/app-release.apk
```

发布包使用 `app/fangshen.jks` 签名（密钥不随仓库分发，请自备 signingConfigs）。

## 数据位置

- 服药记录、自定义药物、预警规则：`<应用专有目录>/files/fangshen/data.json`
- 处方：`<应用专有目录>/files/fangshen/prescriptions/*.json`

## 相关 wiki

机制说明见 [`docs/wiki/`](docs/wiki/)（GitHub Wiki 开通后将同步至 [github.com/…/wiki](https://github.com:inline-function/Function-Manual/wiki)）：药代动力学引擎、效应引擎、联用预警、预警规则、药库结构、数据格式。

## 免责声明

仅面向成年用户的自我观察与记录辅助，不能替代诊断、处方或医嘱。
