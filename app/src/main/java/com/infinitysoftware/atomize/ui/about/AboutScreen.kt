package com.infinitysoftware.atomize.ui.about

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.infinitysoftware.atomize.R

@Composable
fun AboutScreen() {
    val uriHandler = LocalUriHandler.current
    val primaryGreen = MaterialTheme.colorScheme.primary
    val constants = Constants()

    val creditsText = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.Black)) { append(stringResource(R.string.about_animation_by)) }
        pushStringAnnotation("AUTHOR", stringResource(R.string.url_author))
        withStyle(SpanStyle(color = primaryGreen, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)) {
            append(stringResource(R.string.about_author_name))
        }
        pop()
        withStyle(SpanStyle(color = Color.Black)) { append(stringResource(R.string.about_via)) }
        pushStringAnnotation("LOTTIE", stringResource(R.string.url_lottie_files))
        withStyle(SpanStyle(color = primaryGreen, textDecoration = TextDecoration.Underline)) { append(stringResource(R.string.about_lottie_files)) }
        pop()
        withStyle(SpanStyle(color = Color.Black)) { append(stringResource(R.string.about_licensed_under)) }
        pushStringAnnotation("LICENSE", stringResource(R.string.url_lottie_license))
        withStyle(SpanStyle(color = primaryGreen, textDecoration = TextDecoration.Underline)) { append(stringResource(R.string.about_lottie_license)) }
        pop()
        withStyle(SpanStyle(color = Color.Black)) { append(".") }
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
                .padding(horizontal = constants.screenHorizontalPadding, vertical = constants.screenVerticalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.about_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth().padding(all = constants.titlePadding)
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
                    modifier = Modifier.fillMaxWidth().padding(constants.cardPadding),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier.size(constants.iconBoxSize).clip(CircleShape)
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
                            color = Color.Black,
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

@Composable
fun readRawText(resId: Int): String {
    val context = LocalContext.current
    return remember(resId) {
        context.resources.openRawResource(resId).bufferedReader().use { it.readText() }
    }
}

@Composable
fun ExpandablePrivacyPolicyCard() {
    var expanded by remember { mutableStateOf(false) }
    val constants = Constants()
    val privacyTitle = stringResource(R.string.about_privacy_policy_title)
    val privacyText = readRawText(R.raw.privacy_policy)

    Card(
        modifier = Modifier.fillMaxWidth()
            .shadow(
                elevation = constants.cardElevation,
                shape = RoundedCornerShape(constants.cardCornerRadius),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = constants.shadowAlpha)
            )
            .clickable { expanded = !expanded }
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(constants.cardCornerRadius)
    ) {
        Row(modifier = Modifier.padding(constants.cardPadding), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier.size(constants.iconBoxSize).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = privacyTitle,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(constants.iconSize)
                )
            }

            Spacer(modifier = Modifier.width(constants.iconSpacing))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = privacyTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(constants.contentTopSpacing))

                val previewText = if (expanded) privacyText else privacyText.lines().take(constants.privacyPreviewLines).joinToString("\n")
                Text(
                    text = previewText,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = constants.privacyLineHeight),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun ExpandableTermsAndConditionsCard() {
    var expanded by remember { mutableStateOf(false) }
    val constants = Constants()
    val termsTitle = stringResource(R.string.about_terms_and_conditions)
    val termsText = readRawText(R.raw.terms_and_conditions)

    Card(
        modifier = Modifier.fillMaxWidth()
            .shadow(
                elevation = constants.cardElevation,
                shape = RoundedCornerShape(constants.cardCornerRadius),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = constants.shadowAlpha)
            )
            .clickable { expanded = !expanded }
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(constants.cardCornerRadius)
    ) {
        Row(modifier = Modifier.padding(constants.cardPadding), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier.size(constants.iconBoxSize).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Policy,
                    contentDescription = termsTitle,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(constants.iconSize)
                )
            }

            Spacer(modifier = Modifier.width(constants.iconSpacing))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = termsTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(constants.contentTopSpacing))

                val previewText = if (expanded) termsText else termsText.lines().take(constants.privacyPreviewLines).joinToString("\n")
                Text(
                    text = previewText,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = constants.privacyLineHeight),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun UnifiedInfoCard(
    icon: ImageVector,
    title: String,
    content: String,
    iconBackgroundColor: Color,
    isLongText: Boolean = false
) {
    val constants = Constants()

    Card(
        modifier = Modifier.fillMaxWidth()
            .shadow(
                elevation = constants.cardElevation,
                shape = RoundedCornerShape(constants.cardCornerRadius),
                spotColor = iconBackgroundColor.copy(alpha = constants.shadowAlpha)
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(constants.cardCornerRadius)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(constants.cardPadding),
            verticalAlignment = if (isLongText) Alignment.Top else Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(constants.iconBoxSize).clip(CircleShape)
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(constants.iconSize)
                )
            }
            Spacer(modifier = Modifier.width(constants.iconSpacing))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(constants.contentTopSpacing))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    lineHeight = constants.creditsLineHeight
                )
            }
        }
    }
}