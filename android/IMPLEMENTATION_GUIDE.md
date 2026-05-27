# Anywaa Connect – Implementation Guide for UI/UX Design

## Completed: Foundation & Core Components

### ✅ 1. Color Palette (colors.xml & colors-night.xml)
- Dark theme primary colors
- Light theme colors
- Accent colors (teal, cyan)
- Status colors (error, success)
- Message bubble gradients

**Files:**
- `/android/app/src/main/res/values/colors.xml`
- `/android/app/src/main/res/values-night/colors.xml`

### ✅ 2. Material3 Theme Configuration
- `Theme.kt`: Dark/Light color schemes with Material3 mapping
- `Type.kt`: Typography scale (16dp base, Inter font family)
- `Color.kt`: Color constants for Compose

**Files:**
- `/android/app/src/main/java/com/anywaa/connect/ui/theme/Theme.kt`
- `/android/app/src/main/java/com/anywaa/connect/ui/theme/Color.kt`
- `/android/app/src/main/java/com/anywaa/connect/ui/theme/Type.kt`

### ✅ 3. Design System Constants
**File:** `/android/app/src/main/java/com/anywaa/connect/ui/components/DesignSystemConstants.kt`

Exports:
- `Spacing`: 8dp grid (xs=4, sm=8, md=12, lg=16, xl=24, xxl=32)
- `Elevation`: 0, 2, 4, 8dp values
- `ComponentSizes`: standard sizes for sidebars, avatar, buttons, etc.
- `AnimationDurations`: Fast, Normal, Slow, VerySlow

### ✅ 4. Core Components

#### ChatBubble.kt
- AI/user message bubbles with gradient support
- Streaming indicator (three pulsing dots)
- Tool indicator pills
- Citation markers (inline, tappable)
- Action buttons (Copy, Regenerate, Share)
- Timestamp display

#### ThinkingBlock.kt
- Collapsible reasoning steps (DeepSeek style)
- Duration, input/output token metrics
- Expandable detailed reasoning view
- Monospace rendering for code/reasoning

#### CommandMenu.kt
- Popup menu triggered by "/"
- Commands: `/image`, `/think`, `/web`, `/code`, `/project`
- Arrow key navigation, Enter to select
- Icons and descriptions for each command

#### VoiceInputModal.kt
- Full-screen modal for voice input
- Waveform visualizer (20 animated bars)
- Live transcription text area
- Stop and Cancel buttons
- Recording status feedback

#### FunModeToggle.kt
- Three-position slider (Precise | Balanced | Fun)
- Temperature settings (0.3, 0.7, 1.2)
- Icons and labels for each mode
- Badge indicator for active non-default mode

---

## Next Steps: Integration & Layouts

### 📋 TODO: Refactor ChatScreen (Step 4)

**Layout structure:**
```
┌─────────────────────────────────────┐
│ Top Bar (56dp)                      │
│ • Model name + Fun Mode badge       │
│ • Conversation title (editable)     │
│ • Voice input, share, clear buttons │
└─────────────────────────────────────┘
│                                     │
│  Chat Stream (scrollable)           │
│  • Message groups                   │
│  • Date separators                  │
│                                     │
├─────────────────────────────────────┤
│ Input Bar (72dp min, expands)       │
│ • Attach file button (left)         │
│ • Multi-line text field             │
│ • Mic, web search toggles (right)   │
│ • Send button                       │
│ • Command menu (/ trigger)          │
└─────────────────────────────────────┘
```

**Key changes:**
1. Create `ChatScreenTopBar.kt` component
   - Model selector (dropdown)
   - Conversation title with edit capability
   - Voice input button, share, clear

2. Create `ChatInputBar.kt` component
   - Multi-line text field with expansion
   - Attach file button (paperclip icon)
   - Web search toggle (globe icon)
   - Microphone long-press for voice
   - Send button (paper plane)
   - Command menu integration (/ prefix)

3. Refactor `ChatScreen.kt`
   - Integrate new top bar and input bar
   - Add ThinkingBlock and citations to message rendering
   - Auto-scroll on new messages
   - Message grouping by sender

### 📋 TODO: Responsive Layout (Step 5)

**Desktop (>1200dp):**
- Three-column: Left sidebar (260dp) | Chat (center) | Right panel (320dp)
- Sidebars always visible, pinnable

**Tablet (800–1200dp):**
- Left sidebar collapses to 64dp icons
- Right panel slides out on demand (overlay)
- Center chat expands to fill

**Mobile (<600dp):**
- Bottom navigation bar (5 tabs: Chat, Tools, Projects, Activity, Settings)
- Left sidebar as hamburger drawer
- Right panel as swipe-from-right overlay
- Long-press messages for action sheets
- Pull-to-refresh for regenerate

**Implementation:**
1. Create responsive layout state hook
   - Track screen width breakpoints
   - Manage sidebar collapse/expand state
   - Track right panel visibility

2. Create `ResponsiveLayout.kt` wrapper
   - Conditional rendering based on breakpoints
   - Swipe gesture detection for drawers
   - Bottom nav integration

3. Update existing screens for mobile support
   - HomeScreen: grid → single column on mobile
   - SettingsScreen: responsive sections

### 📋 TODO: Keyboard Shortcuts (Step 6)

**Desktop keyboard shortcuts:**
| Action | Shortcut |
|--------|----------|
| New chat | Ctrl+N |
| Focus input | Ctrl+/ |
| Command palette | Ctrl+K |
| Navigate history (up/down) | Ctrl+↑/↓ |
| Submit message | Enter (Ctrl+Enter for newline) |
| Regenerate | Ctrl+R |
| Copy last message | Ctrl+Shift+C |
| Open settings | Ctrl+, |

**Implementation:**
1. Create `KeyboardShortcutsHandler.kt`
   - Use Modifier.onKeyEvent for Compose
   - Map shortcuts to navigation/actions

2. Integrate into ChatScreen
   - Handle all standard shortcuts
   - Document in help/about screen

3. Accessibility note:
   - Test with screen readers
   - Ensure shortcuts don't conflict with accessibility features

### 📋 TODO: Accessibility (Step 7)

**Content Descriptions:**
- All icons: contentDescription = "Action name"
- Chat messages: announce user/AI with timestamp
- Buttons: "Copy message", "Regenerate response", etc.

**Screen Reader Support:**
- Live region (polite) for streaming messages
- Heading hierarchy: H1=app title, H2=sections, H3=subsections
- Table/list semantics for citations and thinking steps

**High Contrast Mode:**
- Respect system setting: increase borders, solid colors, 7:1 contrast ratio
- Implement in `Theme.kt` with system flag check

**Reduced Motion:**
- Respect prefers-reduced-motion system setting
- Disable animations, streaming becomes instant text
- Implement with `LocalDensity` and motion preference

**Implementation:**
1. Add a11y checks in all components
2. Use `Modifier.semantics` for custom behaviors
3. Test with AccessibilityService (TalkBack on Android)

### 📋 TODO: Performance & Testing (Step 8)

**Performance targets:**
- Time to first paint: <1s
- Time to interactive: <2s
- Streaming latency: <200ms from send to first word
- Artifact rendering: <100ms (code highlight), <500ms (diagrams)
- APK size: ≤200MB (with one medium model)

**Optimization checklist:**
1. Lazy composition for chat list (LazyColumn)
2. Remember mutable states to prevent recompositions
3. Use Modifier.drawBehind instead of Surface when possible
4. Profile with Android Studio Profiler
5. APK analyzer: check for unused dependencies
6. Enable code shrinking (minification + R8)

**Testing:**
1. Unit tests for ViewModels (chat logic, message handling)
2. UI tests with Compose testing framework
3. Accessibility tests (Espresso + AccessibilityService)
4. Performance benchmarks with Macrobenchmark
5. Manual device testing across screen sizes

---

## File References

### Created Files
```
android/app/src/main/java/com/anywaa/connect/ui/components/
├── DesignSystemConstants.kt      (Spacing, Elevation, ComponentSizes, AnimationDurations)
├── ChatBubble.kt                 (ChatBubble, TypingIndicator, ToolIndicatorPill, CitationMarker)
├── ThinkingBlock.kt              (ThinkingBlock, ThinkingStepDetail)
├── CommandMenu.kt                (CommandMenu, CommandMenuItem, Command)
├── VoiceInputModal.kt            (VoiceInputModal, WaveformVisualizer)
├── FunModeToggle.kt              (FunModeToggle, FunModeButton, FunModeBadge, FunMode enum)
├── ChatScreenTopBar.kt           (TODO: create next)
├── ChatInputBar.kt               (TODO: create next)
├── ResponsiveLayout.kt           (TODO: create next)
├── KeyboardShortcutsHandler.kt   (TODO: create next)
└── AccessibilityHelpers.kt       (TODO: create next)

android/app/src/main/res/values/
├── colors.xml                    (Updated with full palette)
├── themes.xml                    (Already configured)

android/app/src/main/res/values-night/
└── colors.xml                    (Dark mode overrides)
```

### Updated Files
```
android/app/src/main/java/com/anywaa/connect/ui/theme/
├── Theme.kt                      (Material3 dark/light color schemes)
├── Color.kt                      (Palette constants)
└── Type.kt                       (Typography scale)
```

---

## Integration Checklist

- [ ] Test ChatBubble with sample messages
- [ ] Test ThinkingBlock with sample reasoning steps
- [ ] Test CommandMenu typing "/" in input
- [ ] Test VoiceInputModal recording
- [ ] Test FunModeToggle changing response style
- [ ] Refactor ChatScreen with new components
- [ ] Implement responsive layout detection
- [ ] Add keyboard shortcuts
- [ ] Add accessibility labels
- [ ] Performance test: measure metrics
- [ ] Verify APK size
- [ ] Manual QA across devices (phone, tablet, foldable)
- [ ] Submit to GitHub Actions build

---

## Design Specification References

All components follow the comprehensive UI/UX specification:
- Section 1: Visual Identity & Theme
- Section 2: Layout Structure (desktop/tablet/mobile)
- Section 3: Core Components (implemented above)
- Section 4: Interaction Flows
- Section 5: Responsive Behavior
- Section 6: Accessibility & Keyboard Shortcuts
- Section 7: Performance Targets
- Section 8: Implementation Notes

---

## Next Development Cycle

Once core components and layouts are integrated:
1. **Artifact viewing** (code preview, diagram rendering, table UI)
2. **Projects/knowledge base UI** (upload, list, manage documents)
3. **Settings refinement** (theme selector, language, font size)
4. **Analytics & error tracking** (Sentry integration)
5. **Beta testing with real users**

---

## Questions or Blockers?

- Need to integrate with existing ViewModels? Check `ChatViewModel.kt`, `ThemeViewModel.kt`
- Issues with Material3 colors? Cross-reference `DarkColorScheme` / `LightColorScheme` in `Theme.kt`
- Performance concerns? Profile with Android Studio Profiler, measure first frame render time
- Accessibility compliance? Test with TalkBack and Accessibility Inspector

