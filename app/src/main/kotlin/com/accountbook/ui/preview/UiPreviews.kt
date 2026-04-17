package com.accountbook.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.accountbook.ui.component.LedgerTopBar
import com.accountbook.ui.component.MonthSelector
import com.accountbook.ui.component.SummaryCard
import com.accountbook.ui.theme.AccountBookTheme

@Preview(showBackground = true, name = "Top Bar")
@Composable
fun TopBarPreview() {
    AccountBookTheme {
        LedgerTopBar(
            title = "LEDGER.SHOCK",
            rightContent = {}
        )
    }
}

@Preview(showBackground = true, name = "Month Selector")
@Composable
fun MonthSelectorPreview() {
    AccountBookTheme {
        MonthSelector(
            month = "2026-04",
            onPrevious = {},
            onNext = {}
        )
    }
}

@Preview(showBackground = true, name = "Summary Card")
@Composable
fun SummaryCardPreview() {
    AccountBookTheme {
        SummaryCard(
            incomeTotal = 15000.0,
            expenseTotal = 2420.0,
            balance = 12580.0
        )
    }
}
