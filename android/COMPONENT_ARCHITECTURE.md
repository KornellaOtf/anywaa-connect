# Anywaa Connect – Component Architecture & Usage Guide

## Component Hierarchy

```
AnywaaConnectTheme (Theme.kt)
├── DarkColorScheme / LightColorScheme
├── Typography (Type.kt)
├── Shapes
└── Content
    ├── ChatScreen (main chat interface)
    │   ├── ChatScreenTopBar (TODO)
    │   │   ├── Model selector dropdown
    │   │   ├── FunModeBadge (if active)
    │   │   └── Action buttons (voice, share, clear)
    │   ├── ChatArea
    │   │   ├── DateSeparator
    │   │   ├── MessageGroup
    │   │   │   ├── ChatBubble (AI message)
    │   │   │   │   ├── Avatar
    │   │   │   │   ├── Message text (streaming)
    │   │   │   │   ├── ToolIndicatorPill
    │   │   │   │   ├── CitationMarker [1], [2]...
    │   │   │   │   └── Action buttons
    │   │   │   └── ChatBubble (user message)
    │   │   ├── ThinkingBlock (when AI reasoning shown)
    │   │   │   ├── Collapsed header
    │   │   │   └── ThinkingStepDetail (expanded)
    │   │   └── Divider
    │   └── ChatInputBar (TODO)
    │       ├── Attach file button
    │       ├── TextInputField (multi-line)
    │       ├── WebSearch toggle
    │       ├── Microphone button
    │       ├── Send button
    │       └── CommandMenu (/ trigger)
    │           └── CommandMenuItem (×5 options)
    ├── VoiceInputModal (when recording)
    │   ├── WaveformVisualizer
    │   ├── LiveTranscriptionArea
    │   └── Control buttons (Stop, Cancel)
    └── RightPanel (TODO, contextual)
        ├── Artifacts tab
        ├── Thinking tab
        └── Sources tab
```

---

## Component Import Paths

```kotlin
// Design system
import com.anywaa.connect.ui.components.Spacing
import com.anywaa.connect.ui.components.Elevation
import com.anywaa.connect.ui.components.ComponentSizes
import com.anywaa.connect.ui.components.AnimationDurations

// Chat components
import com.anywaa.connect.ui.components.ChatBubble
import com.anywaa.connect.ui.components.Citation
import com.anywaa.connect.ui.components.CitationConfidence
import com.anywaa.connect.ui.components.CitationMarker
import com.anywaa.connect.ui.components.CitationPopup
import com.anywaa.connect.ui.components.ToolIndicatorPill
import com.anywaa.connect.ui.components.TypingIndicator

// Reasoning
import com.anywaa.connect.ui.components.ThinkingBlock
import com.anywaa.connect.ui.components.ThinkingStep
import com.anywaa.connect.ui.components.ThinkingStepDetail

// Voice
import com.anywaa.connect.ui.components.VoiceInputModal
import com.anywaa.connect.ui.components.WaveformVisualizer

// Commands
import com.anywaa.connect.ui.components.CommandMenu
import com.anywaa.connect.ui.components.Command

// Fun Mode
import com.anywaa.connect.ui.components.FunModeToggle
import com.anywaa.connect.ui.components.FunMode
import com.anywaa.connect.ui.components.FunModeBadge

// Theme colors
import com.anywaa.connect.ui.theme.DarkBackgroundPrimary
import com.anywaa.connect.ui.theme.DarkPrimaryAccent
import com.anywaa.connect.ui.theme.LightBackgroundPrimary
import com.anywaa.connect.ui.theme.UserBubbleGradientStart
import com.anywaa.connect.ui.theme.UserBubbleGradientEnd
```

---

## Quick Usage Examples

### Display AI Message with Thinking & Citations

```kotlin
// State
var message by remember { mutableStateOf("Here's my analysis...") }
var thinkingSteps by remember { mutableStateOf(listOf<ThinkingStep>()) }
var citations by remember { mutableStateOf(listOf<Citation>()) }

// Render thinking block (before message)
if (thinkingSteps.isNotEmpty()) {
    ThinkingBlock(
        thinkingSteps = thinkingSteps,
        totalDuration = 2300
    )
}

// Render message
ChatBubble(
    message = message,
    isUser = false,
    timestamp = "2 min ago",
    isStreaming = false,
    toolIndicator = null,
    citations = citations,
    onCopy = { /* copy to clipboard */ },
    onRegenerate = { /* regenerate response */ },
    onShare = { /* share message */ }
)
```

### Display User Message

```kotlin
ChatBubble(
    message = "Generate an image of a cat in space",
    isUser = true,
    timestamp = "Now",
    toolIndicator = null,
    citations = emptyList()
)
```

### Show Tool Indicator (Image Generation)

```kotlin
ChatBubble(
    message = "[Generated image with prompt...]",
    isUser = false,
    timestamp = "1 min ago",
    toolIndicator = "Image Generation", // Shows as pill above message
    onCopy = { },
    onRegenerate = { }
)
```

### Use Command Menu

```kotlin
var commandText by remember { mutableStateOf("") }
var showCommandMenu by remember { mutableStateOf(false) }

// Trigger when "/" typed
LaunchedEffect(commandText) {
    showCommandMenu = commandText.endsWith("/")
}

CommandMenu(
    isVisible = showCommandMenu,
    onCommandSelected = { command ->
        commandText = command
        showCommandMenu = false
    },
    onDismiss = { showCommandMenu = false }
)
```

### Voice Input Recording

```kotlin
var isRecording by remember { mutableStateOf(false) }
var transcription by remember { mutableStateOf("") }
var waveform by remember { mutableStateOf(listOf<Float>()) }

VoiceInputModal(
    isVisible = showVoiceModal,
    isRecording = isRecording,
    liveTranscription = transcription,
    waveformAmplitudes = waveform,
    onStop = {
        isRecording = false
        // send transcription as message
    },
    onCancel = { showVoiceModal = false }
)
```

### Fun Mode Toggle

```kotlin
var funMode by remember { mutableStateOf(FunMode.BALANCED) }

FunModeToggle(
    selectedMode = funMode,
    onModeChanged = { newMode ->
        funMode = newMode
        // Update model temperature: newMode.temperature
    }
)

// Show badge if not balanced
if (funMode != FunMode.BALANCED) {
    FunModeBadge(mode = funMode)
}
```

### Using Design Constants

```kotlin
// Spacing
Box(
    modifier = Modifier.padding(
        horizontal = Spacing.lg,
        vertical = Spacing.md
    )
)

// Elevation
Surface(
    tonalElevation = Elevation.standard
)

// Sizes
Icon(
    modifier = Modifier.size(ComponentSizes.iconMedium)
)

// Animations
animateContentSize(
    animationSpec = tween(
        durationMillis = AnimationDurations.Normal
    )
)
```

---

## Color Mapping (Material3)

```kotlin
// Primary accent (Teal)
MaterialTheme.colorScheme.primary           // #00BFA5 (dark) / #00897B (light)
MaterialTheme.colorScheme.onPrimary         // White (text on primary)
MaterialTheme.colorScheme.primaryContainer  // Lighter teal for backgrounds

// Secondary accent (Cyan)
MaterialTheme.colorScheme.secondary         // #00E5FF (dark) / #0097A7 (light)
MaterialTheme.colorScheme.onSecondary       // Black/dark text

// Surfaces
MaterialTheme.colorScheme.background        // #0A0A1F (dark) / #F5F5F7 (light)
MaterialTheme.colorScheme.surface           // #12121A (dark) / #FFFFFF (light)
MaterialTheme.colorScheme.surfaceVariant    // #1E1E2E (dark) / #F0F0F2 (light)

// Text
MaterialTheme.colorScheme.onSurface         // #FFFFFF (dark) / #1A1A1E (light)
MaterialTheme.colorScheme.onSurfaceVariant  // #A0A0B0 (dark) / #5A5A6E (light)

// Error/Success
MaterialTheme.colorScheme.error             // #FF5C5C (both themes)
MaterialTheme.colorScheme.errorContainer    // Light error for backgrounds
MaterialTheme.colorScheme.outline           // #2A2A35 (dark) / #D0D0D8 (light)
```

---

## Best Practices

### ✅ DO

- Use `Spacing` constants for all padding/margin
  ```kotlin
  ✅ modifier = Modifier.padding(Spacing.lg)
  ✅ modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm)
  ```

- Use theme colors, never hardcode
  ```kotlin
  ✅ color = MaterialTheme.colorScheme.primary
  ✅ tint = MaterialTheme.colorScheme.onSurface
  ```

- Add `contentDescription` to all icons
  ```kotlin
  ✅ Icon(..., contentDescription = "Copy message")
  ```

- Use `remember` for mutable state in composables
  ```kotlin
  ✅ var isExpanded by remember { mutableStateOf(false) }
  ```

- Lazy load long lists
  ```kotlin
  ✅ LazyColumn { items(messages) { ... } }
  ```

### ❌ DON'T

- Hardcode colors
  ```kotlin
  ❌ color = Color(0xFF00BFA5)
  ```

- Hardcode spacing values
  ```kotlin
  ❌ modifier = Modifier.padding(16.dp)
  ```

- Use `LazyListState` at Composable top level
  ```kotlin
  ❌ val listState = rememberLazyListState()
  ✅ val listState by remember { LazyListState() }
  ```

- Use `var` for state without `remember`
  ```kotlin
  ❌ var message = "..."
  ✅ var message by remember { mutableStateOf("...") }
  ```

---

## Testing Components Individually

### Preview in Android Studio

```kotlin
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ChatBubblePreview() {
    AnywaaConnectTheme {
        ChatBubble(
            message = "This is a sample AI response with reasoning.",
            isUser = false,
            timestamp = "2 min ago",
            citations = listOf(
                Citation(
                    title = "Sample Source",
                    url = "https://example.com",
                    snippet = "This is a sample snippet..."
                )
            ),
            onCopy = { },
            onRegenerate = { },
            onShare = { }
        )
    }
}
```

### Manual Testing Checklist

- [ ] Dark theme: all colors visible, readable
- [ ] Light theme: all colors visible, readable
- [ ] Streaming text: appears correctly
- [ ] Citations: tappable, popup shows
- [ ] Thinking block: expands/collapses smoothly
- [ ] Command menu: appears with "/" input
- [ ] Voice modal: waveform animates, transcription updates
- [ ] Fun mode toggle: switches between 3 positions
- [ ] Spacing: consistent padding, no overlaps
- [ ] Responsiveness: works on phones, tablets, desktops

---

## Performance Notes

### Memory
- LazyColumn auto-disposes off-screen composables
- `remember` blocks are only recomposed if dependencies change
- Avoid `collectAsState()` without proper scoping

### Rendering
- Use `Modifier.drawBehind` instead of `Surface` for simple backgrounds
- Avoid nested `Surface` components (use `modifier` instead)
- Profile with Android Studio Profiler if slow

### Animations
- Use `animateFloatAsState` / `animateColorAsState` for simple values
- Respect `prefers-reduced-motion` system setting
- Profile animation performance on low-end devices

---

## Further Reading

- Jetpack Compose Documentation: https://developer.android.com/develop/ui/compose
- Material 3 Design System: https://m3.material.io/
- Anywaa Specification: See `/android/COMPLETE_UI_UX_SPECIFICATION.md`
- Implementation Guide: See `/android/IMPLEMENTATION_GUIDE.md`

