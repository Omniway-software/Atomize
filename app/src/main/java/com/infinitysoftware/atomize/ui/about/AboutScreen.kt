package com.infinitysoftware.atomize.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.infinitysoftware.atomize.R
import com.infinitysoftware.atomize.ui.about.composables.ExpandablePrivacyPolicyCard
import com.infinitysoftware.atomize.ui.about.composables.ExpandableTermsAndConditionsCard
import com.infinitysoftware.atomize.ui.about.composables.UnifiedInfoCard
import com.infinitysoftware.atomize.ui.theme.Black

@Composable
fun AboutScreen() {
    val uriHandler = LocalUriHandler.current
    val primaryGreen = MaterialTheme.colorScheme.primary
    val constants = Constants()

    val creditsText = buildAnnotatedString {
        withStyle(SpanStyle(color = Black)) { append(stringResource(R.string.about_animation_by)) }
        pushStringAnnotation("AUTHOR", stringResource(R.string.url_author))
        withStyle(SpanStyle(color = primaryGreen, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)) {
            append(" ")
            append(stringResource(R.string.about_author_name))
            append(" ")
        }
        pop()
        withStyle(SpanStyle(color = Black)) { append(stringResource(R.string.about_via)) }
        pushStringAnnotation("LOTTIE", stringResource(R.string.url_lottie_files))
        withStyle(SpanStyle(color = primaryGreen, textDecoration = TextDecoration.Underline)) {
            append(" ")
            append(stringResource(R.string.about_lottie_files))
        }
        pop()
        withStyle(SpanStyle(color = Black)) { append(stringResource(R.string.about_licensed_under)) }
        pushStringAnnotation("LICENSE", stringResource(R.string.url_lottie_license))
        withStyle(SpanStyle(color = primaryGreen, textDecoration = TextDecoration.Underline)) {
            append(" ")
            append(stringResource(R.string.about_lottie_license))
        }
        pop()
        withStyle(SpanStyle(color = Black)) { append(".") }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = constants.surfaceVariantAlpha)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = constants.screenHorizontalPadding,
                    vertical = constants.screenVerticalPadding
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.about_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = constants.titlePadding)
            )

            Spacer(modifier = Modifier.height(constants.titleBottomSpacing))

            UnifiedInfoCard(
                icon = Icons.Filled.Person,
                title = stringResource(R.string.about_application_by),
                content = stringResource(R.string.about_developer_name),
                iconBackgroundColor = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(constants.cardSpacing))

            ExpandablePrivacyPolicyCard()

            Spacer(modifier = Modifier.height(constants.cardSpacing))

            ExpandableTermsAndConditionsCard()

            Spacer(modifier = Modifier.height(constants.cardSpacing))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = constants.cardElevation,
                        shape = RoundedCornerShape(constants.cardCornerRadius),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = constants.shadowAlpha)
                    ),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(constants.cardCornerRadius)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(constants.cardPadding),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(constants.iconBoxSize)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Animation,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(constants.iconSize)
                        )
                    }
                    Spacer(modifier = Modifier.width(constants.iconSpacing))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.about_animation_credits),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Black,
                            modifier = Modifier.padding(bottom = constants.creditsBottomSpacing)
                        )
                        ClickableText(
                            text = creditsText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = constants.creditsFontSize,
                                lineHeight = constants.creditsLineHeight
                            ),
                            onClick = { offset ->
                                creditsText.getStringAnnotations("AUTHOR", offset, offset).firstOrNull()?.let { uriHandler.openUri(it.item) }
                                creditsText.getStringAnnotations("LOTTIE", offset, offset).firstOrNull()?.let { uriHandler.openUri(it.item) }
                                creditsText.getStringAnnotations("LICENSE", offset, offset).firstOrNull()?.let { uriHandler.openUri(it.item) }
                            }
                        )
                    }
                }
            }
        }
    }
}