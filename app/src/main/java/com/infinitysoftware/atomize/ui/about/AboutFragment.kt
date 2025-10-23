package com.infinitysoftware.atomize.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutFragment() {
    val uriHandler = LocalUriHandler.current
    val primaryGreen = MaterialTheme.colorScheme.primary
    val creditsText = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.Black)) {
            append("'Streak Icon [Fire]' animation by ")
        }
        pushStringAnnotation(tag = "AUTHOR", annotation = "https://lottiefiles.com/twlt1o42jpm59mva")
        withStyle(
            SpanStyle(
                color = primaryGreen,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("Mohsen Zamani")
        }
        pop()
        withStyle(SpanStyle(color = Color.Black)) {
            append(" via ")
        }
        pushStringAnnotation(tag = "LOTTIE", annotation = "https://lottiefiles.com")
        withStyle(
            SpanStyle(
                color = primaryGreen,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("LottieFiles")
        }
        pop()
        withStyle(SpanStyle(color = Color.Black)) {
            append(", licensed under the ")
        }

        pushStringAnnotation(tag = "LICENSE", annotation = "https://lottiefiles.com/page/license")
        withStyle(
            SpanStyle(
                color = primaryGreen,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Lottie License")
        }
        pop()

        withStyle(SpanStyle(color = Color.Black)) {
            append(".")
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            UnifiedInfoCard(
                icon = Icons.Filled.Person,
                title = "Application By:",
                content = "Dušan Rosić",
                iconBackgroundColor = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            UnifiedInfoCard(
                icon = Icons.Filled.Star,
                title = "Special Thanks To:",
                content = "Djordje Stanišić – whose guidance and feedback greatly contributed to this project.",
                iconBackgroundColor = MaterialTheme.colorScheme.primary,
                isLongText = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Animation,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Animation Credits:",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        ClickableText(
                            text = creditsText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                lineHeight = 22.sp
                            ),
                            onClick = { offset ->
                                creditsText.getStringAnnotations("AUTHOR", offset, offset)
                                    .firstOrNull()?.let { uriHandler.openUri(it.item) }
                                creditsText.getStringAnnotations("LOTTIE", offset, offset)
                                    .firstOrNull()?.let { uriHandler.openUri(it.item) }
                                creditsText.getStringAnnotations("LICENSE", offset, offset)
                                    .firstOrNull()?.let { uriHandler.openUri(it.item) }
                            }
                        )
                    }
                }
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = iconBackgroundColor.copy(alpha = 0.5f)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = if (isLongText) Alignment.Top else Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    lineHeight = 22.sp
                )
            }
        }
    }
}