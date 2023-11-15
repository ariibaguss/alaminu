import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alaminu.databinding.FragmentModulBinding
import com.google.android.material.tabs.TabLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.alaminu.R
import com.example.alaminu.ui.modul.LatihanFragment
import com.example.alaminu.ui.modul.MateriFragment
import com.example.alaminu.ui.modul.SemuaFragment
import com.google.android.material.tabs.TabLayoutMediator

class ModulFragment : Fragment() {

    private var _binding: FragmentModulBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModulBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewPager = root.findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = root.findViewById<TabLayout>(R.id.tabLayout)

        val pagerAdapter = ModulPagerAdapter(requireActivity())
        pagerAdapter.addFragment(SemuaFragment())
        pagerAdapter.addFragment(MateriFragment())
        pagerAdapter.addFragment(LatihanFragment())

        viewPager.adapter = pagerAdapter

        // Hubungkan TabLayout dengan ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Atur teks tab sesuai dengan posisi fragment
            tab.text = when (position) {
                0 -> "Semua"
                1 -> "Materi"
                2 -> "Latihan"
                else -> null
            }
        }.attach()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
