# NFL Draft Trade Tool

## Introduction
Welcome to the NFL Draft Trade Tool! This aims to be a simple, command-line application designed to evaluate if prospective trades between teams concerning draft capital is fair.

We give the user the option to decide which grading metric they would like to use, between pre-defined models:
- [Jimmy Johnson's Trade Value Model](https://www.drafttek.com/NFL-Trade-Value-Chart.asp)
- [Rich Hill's Trade Value Model (Revised)](https://www.drafttek.com/NFL-Trade-Value-Chart-Rich-Hill.asp)

> [!NOTE]
> It is worth noting that it is believed that teams are using a revised trade value model, which has been modelled by Rich Hill.
> Jimmy Johnson's trade value model is still accepted but the scaling may not be accurate.

## How to use
The NFL Draft Trade Tool is designed to be easy to use, and is ran via the CLI. We may look to introduce a UI down the line but currently this is not on the project roadmap.

## Maintaining

### Weights
Both trade models can be manually edited by changing the respective file (either `jimmy-johnson-draft-chart.csv`, or `rich-hill-draft-chart.csv`), using the following format:
Users are advised this file is expected to be a CSV with the following format: `Round,Pick,Weight`

Example: Early Round Pick
```csv
# Round One
1,1,3000
```

Example: Late Round Pick
```csv
# Round Five
5,171,9
```

### Draft Order
Users are able to freely edit the draft order file (`picks.csv`) to ensure that this tool can be used for different drafts, and is not locked to the 2025 NFL Draft.

Users are advised this file is expected to be a CSV with the following format: `Round,Pick,Team (Abbreviated)`

```csv
# Round Two
2,33,CLE
2,34,NYG
2,35,TEN
2,36,JAX
```